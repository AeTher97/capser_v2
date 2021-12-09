package com.mwozniak.capser_v2.ytremote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwozniak.capser_v2.ytremote.enums.MemberType;
import com.mwozniak.capser_v2.ytremote.models.MemberSession;
import com.mwozniak.capser_v2.ytremote.models.RemoteSession;
import com.mwozniak.capser_v2.ytremote.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sun.rmi.server.InactiveGroupException;

import java.io.IOException;
import java.util.*;

@Service
@Log4j
public class WebSocketSessionManager {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<User, RemoteSession> remoteSessions;

    public WebSocketSessionManager() {
        this.remoteSessions = new HashMap<>();
    }

    public void registerSession(User user) {
        RemoteSession remoteSession = new RemoteSession(user, objectMapper);
        remoteSessions.put(user, remoteSession);
    }

    public void initializeSession(User user, WebSocketSession webSocketSession, String deviceId, MemberType memberType) {
        try {
            remoteSessions.get(user).addMemberSession(webSocketSession, deviceId, memberType);
        } catch (IOException e) {
            log.error("Failed to notify receiver" + e.getMessage());
        }
    }

    public RemoteSession getRemoteSession(User user) {
        return remoteSessions.get(user);
    }

    public RemoteSession getRemoteSessionByMemberSession(WebSocketSession webSocketSession) {
        for (Map.Entry<User, RemoteSession> entry : remoteSessions.entrySet()) {
            for (Map.Entry<String, MemberSession> entry1 : entry.getValue().getMemberSessions().entrySet()) {
                if (entry1.getValue().getWebSocketSession().equals(webSocketSession)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Scheduled(fixedRate = 60000)
    public void garbageCollectSessions() {
        int deletedMemberSessions = 0;
        int deletedRemoteSessions = 0;
        log.info("Collecting garbage sessions." + new Date());

        List<User> removedRemoteSessions = new ArrayList<>();
        for (Map.Entry<User, RemoteSession> remoteSession : remoteSessions.entrySet()) {
            List<String> removedMemberSessions = new ArrayList<>();
            for (Map.Entry<String, MemberSession> memberSession : remoteSession.getValue().getMemberSessions().entrySet()) {
                if (new Date().getTime() - memberSession.getValue().getLastActive().getTime() > 60000) {
                    sendError(new InactiveGroupException("This session was inactive for too long!"), memberSession.getValue().getWebSocketSession());
                    removedMemberSessions.add(memberSession.getKey());
                    deletedMemberSessions++;
                }
            }
            for (String id : removedMemberSessions) {
                remoteSession.getValue().removeMemberSession(id);

            }

            if (remoteSession.getValue().getMemberSessions().size() == 0) {
                removedRemoteSessions.add(remoteSession.getKey());
                deletedRemoteSessions++;
            }
        }

        for (User user : removedRemoteSessions) {
            remoteSessions.remove(user);
        }
        log.info("Garbage collected " + deletedRemoteSessions + " remote sessions and " + deletedMemberSessions + " member sessions");
        log.info("Remaining remote sessions " + remoteSessions.size());
    }

    private void sendError(Exception e, WebSocketSession webSocketSession) {
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Error(e.getMessage()))));
        } catch (IOException ev) {
            log.error("Failed sending error message");
        }
    }
}

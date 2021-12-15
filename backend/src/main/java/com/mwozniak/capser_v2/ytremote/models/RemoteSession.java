package com.mwozniak.capser_v2.ytremote.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwozniak.capser_v2.ytremote.enums.MemberType;
import com.mwozniak.capser_v2.ytremote.models.messages.server.CurrentReceiver;
import com.mwozniak.capser_v2.ytremote.models.messages.server.Receivers;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j2
public class RemoteSession {

    private final Map<String, MemberSession> memberSessions;
    private final User user;
    private final ObjectMapper objectMapper;
    private MemberSession mediaPlayer;

    public RemoteSession(User user, ObjectMapper objectMapper) {
        this.memberSessions = new HashMap<>();
        this.user = user;
        this.objectMapper = objectMapper;
    }

    public void addMemberSession(WebSocketSession webSocketSession, String deviceName, MemberType memberType) throws IOException {
        MemberSession memberSession = new MemberSession();
        memberSession.setMemberType(memberType);
        memberSession.setWebSocketSession(webSocketSession);
        memberSession.setDeviceName(deviceName);

        memberSessions.put(deviceName, memberSession);

        if (memberType.equals(MemberType.RECEIVER) && mediaPlayer == null) {
            mediaPlayer = memberSession;
        }

        emitReceivers();

        notifyReceivers();
    }

    public void removeMemberSession(String deviceName) {
        if (memberSessions.get(deviceName) == null) {
            log.error("This session doesn't exist");
            return;
        }
        if (memberSessions.get(deviceName).equals(mediaPlayer)) {
            mediaPlayer = null;
        }
        try {
            memberSessions.get(deviceName).getWebSocketSession().close();
        } catch (IOException e) {
            log.error("Failed to close session");
        }
        memberSessions.remove(deviceName);
        if (!getReceiversNames().isEmpty() && mediaPlayer == null) {
            mediaPlayer = memberSessions.get(getReceiversNames().get(0));
        }

        notifyReceivers();
        emitReceivers();

    }

    public MemberSession getMemberSession(String deviceName) {
        return memberSessions.get(deviceName);
    }

    public String getMemberSessionDeviceName(WebSocketSession webSocketSession) {
        for (Map.Entry<String, MemberSession> entry : memberSessions.entrySet()) {
            if (entry.getValue().getWebSocketSession().equals(webSocketSession)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Map<String, MemberSession> getMemberSessions() {
        return memberSessions;
    }

    public User getUser() {
        return user;
    }

    public MemberSession getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(String deviceName) throws NotFoundException, IOException {
        if (getMemberSession(deviceName) != null) {
            this.mediaPlayer = getMemberSession(deviceName);
            notifyReceivers();
        } else {
            throw new NotFoundException("Receiver not found exception");
        }
    }

    public List<Receiver> getReceivers() {
        return getReceiversNames().stream().map(receiverName -> {
            Receiver receiver = new Receiver();
            receiver.setDeviceName(receiverName);
            return receiver;
        }).collect(Collectors.toList());
    }

    private List<String> getReceiversNames() {
        List<String> receiversList = new ArrayList<>();
        for (Map.Entry<String, MemberSession> entry : memberSessions.entrySet()) {
            if (entry.getValue().getMemberType().equals(MemberType.RECEIVER)) {
                receiversList.add(entry.getKey());
            }
        }
        return receiversList;
    }

    public List<MemberSession> getControllers() {
        return memberSessions.values().stream().filter(memberSession -> memberSession.getMemberType().equals(MemberType.CONTROLLER)).collect(Collectors.toList());
    }

    private void notifyReceivers() {
        try {
            CurrentReceiver currentReceiver = new CurrentReceiver();
            if (mediaPlayer != null) {
                currentReceiver.setDeviceName(getMemberSessionDeviceName(mediaPlayer.getWebSocketSession()));
            } else {
                currentReceiver.setDeviceName(null);
            }

            TextMessage message = new TextMessage(objectMapper.writeValueAsString(currentReceiver));
            getReceiversNames().forEach(receiver -> {
                try {
                    getMemberSession(receiver).getWebSocketSession().sendMessage(message);
                } catch (IllegalStateException e) {
                    log.error("Failed to emit receivers to one of the sessions");
                    removeMemberSession(receiver);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            getControllers().forEach(controller -> {
                try {
                    controller.getWebSocketSession().sendMessage(message);
                } catch (IllegalStateException e) {
                    log.error("Failed to emit receivers to one of the sessions");
                    removeMemberSession(controller.getDeviceName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed writing message");
        }
    }

    private void emitReceivers() {
        Receivers receivers = new Receivers();
        receivers.setReceivers(getReceivers());
        try {
            TextMessage receiversMessage = new TextMessage(objectMapper.writeValueAsString(receivers));

            getControllers().forEach(controller -> {
                try {
                    controller.getWebSocketSession().sendMessage(receiversMessage);
                } catch (IllegalStateException e) {
                    log.error("Failed to emit receivers to one of the sessions");
                    removeMemberSession(controller.getDeviceName());
                } catch (IOException e) {
                    log.error("Failed to emit receivers");
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed writing message");
        }
    }
}

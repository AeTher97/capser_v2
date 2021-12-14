package com.mwozniak.capser_v2.ytremote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mwozniak.capser_v2.ytremote.enums.MessageType;
import com.mwozniak.capser_v2.ytremote.models.MemberSession;
import com.mwozniak.capser_v2.ytremote.models.RemoteSession;
import com.mwozniak.capser_v2.ytremote.models.User;
import com.mwozniak.capser_v2.ytremote.models.messages.client.BasicMessage;
import com.mwozniak.capser_v2.ytremote.models.messages.client.SetReceiver;
import com.mwozniak.capser_v2.ytremote.models.messages.client.Start;
import com.mwozniak.capser_v2.ytremote.models.messages.client.Stop;
import com.mwozniak.capser_v2.ytremote.models.messages.server.Error;
import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Log4j
public class YouTubeHandler implements WebSocketHandler, SubProtocolCapable {


    private ObjectMapper objectMapper;
    private WebSocketSessionManager webSocketSessionManager;

    public YouTubeHandler(WebSocketSessionManager webSocketSessionManager) {
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    public List<String> getSubProtocols() {
        List<String> subProtocols = new ArrayList<>();
        subProtocols.add("remote");
        return subProtocols;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        User user = new User();
        user.setUsername("Marek");
        if (webSocketSessionManager.getRemoteSession(user) == null) {
            webSocketSessionManager.registerSession(user);
        }
        log.debug("Established new connection with session for user: " + user.getUsername());
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> message) throws Exception {
//        System.out.println(message.getPayload());

        User user = new User();
        user.setUsername("Marek");

        try {
            BasicMessage basicMessage = objectMapper.readValue(message.getPayload().toString(), BasicMessage.class);
            MessageType messageType = basicMessage.getMessageType();


            if (messageType == null) {
                log.debug("Message type is null no action performed");
                sendError(new IllegalStateException("Message type is null no action performed"), webSocketSession);
                return;
            }

            if (webSocketSessionManager.getRemoteSessionByMemberSession(webSocketSession) == null && messageType != MessageType.START) {
                sendError(new IllegalStateException("This session is not initialized"), webSocketSession);
                return;
            }

            switch (messageType) {
                case START:
                    handleStart(message, webSocketSession, user);
                    break;
                case MEDIA_CONTROL:
                case QUEUE_CONTROL:
                case SEARCH_CONTROL:
                    handleMediaControl(message, user, webSocketSession);
                    break;
                case SET_RECEIVER:
                    handleSetReceiver(message, user, webSocketSession);
                    break;
                case CONTROLS_SONG:
                case CONTROLS_TIME:
                case CONTROLS_DETAILS:
                case HOME:
                case QUEUE:
                case QUEUE_LENGTH:
                    handleMediaState(message, user, webSocketSession);
                    break;
                case STOP:
                    handleStop(message, user, webSocketSession);
                    break;
            }

            updateLastSeen(user, webSocketSession);
        } catch (JsonParseException | JsonMappingException e) {
            sendError(e, webSocketSession);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        log.error("Transport error");
        System.out.println(throwable.getMessage());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {

        log.debug("Closed session with: " + webSocketSession.getId());
        RemoteSession remoteSession = webSocketSessionManager.getRemoteSessionByMemberSession(webSocketSession);
        if (remoteSession != null) {
            log.debug("user: " + remoteSession.getUser().getUsername());
            if (remoteSession.getMemberSessionDeviceName(webSocketSession) != null) {
                remoteSession.removeMemberSession(remoteSession.getMemberSessionDeviceName(webSocketSession));
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void updateLastSeen(User user, WebSocketSession webSocketSession) {
        RemoteSession remoteSession = webSocketSessionManager.getRemoteSession(user);
        if (remoteSession == null) {
            log.debug("Remote session doesn't exist");
            return;
        }
        MemberSession memberSession = remoteSession.getMemberSession(remoteSession.getMemberSessionDeviceName(webSocketSession));
        if (memberSession == null) {
            log.debug("Member session doesn't exist");
            return;
        }
        memberSession.setLastActive(new Date());
    }

    private void handleStart(WebSocketMessage<?> message, WebSocketSession webSocketSession, User user) throws IOException {
        Start start = objectMapper.readValue(message.getPayload().toString(), Start.class);

        if (webSocketSessionManager.getRemoteSession(user).getMemberSessions().containsKey(start.getDeviceName())) {
            sendError(new NameAlreadyBound("This name is already in use"), webSocketSession);
            webSocketSession.close();
            return;
        }

        webSocketSessionManager.initializeSession(user, webSocketSession, start.getDeviceName(), start.getMemberType());

    }

    private void handleMediaControl(WebSocketMessage<?> message, User user, WebSocketSession webSocketSession) throws IOException {
        webSocketSessionManager.getRemoteSession(user).getMediaPlayer().getWebSocketSession().sendMessage(message);
    }

    private void handleStop(WebSocketMessage<?> message, User user, WebSocketSession webSocketSession) throws IOException {
        Stop stop = objectMapper.readValue(message.getPayload().toString(), Stop.class);
        webSocketSessionManager.getRemoteSession(user).removeMemberSession(stop.getDeviceName());
        log.debug("Removed member session of user: " + user.getUsername() + " device: " + stop.getDeviceName());
    }

    private void handleSetReceiver(WebSocketMessage<?> message, User user, WebSocketSession webSocketSession) throws IOException {
        SetReceiver setReceiver = objectMapper.readValue(message.getPayload().toString(), SetReceiver.class);
        RemoteSession remoteSession = webSocketSessionManager.getRemoteSession(user);
        try {
            remoteSession.setMediaPlayer(setReceiver.getDeviceName());
        } catch (NotFoundException e) {
            sendError(e, webSocketSession);
        }
    }


    private void handleMediaState(WebSocketMessage<?> message, User user, WebSocketSession webSocketSession) {
        webSocketSessionManager.getRemoteSession(user).getControllers().forEach(
                controller -> {
                    try {
                        controller.getWebSocketSession().sendMessage(message);
                    } catch (IllegalStateException | IOException e) {
                        log.error("Failed to emit receivers to one of the controllers");
                        webSocketSessionManager.getRemoteSession(user).removeMemberSession(controller.getDeviceName());
                        log.error(e.getMessage());
                    }
                }
        );
    }

    private void sendError(Exception e, WebSocketSession webSocketSession) throws IOException {
        webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(new Error(e.getMessage()))));
    }
}

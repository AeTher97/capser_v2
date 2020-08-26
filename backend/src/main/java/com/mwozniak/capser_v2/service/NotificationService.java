package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notify(Notification notification){
        notificationRepository.save(notification);
    }

    public void notify(AcceptanceRequest acceptanceRequest, String otherUser){

        String text;

        if(acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)){
            text = "Your game with user " + otherUser + " is waiting to be accepted";
        } else {
            text = "You have a game with user " + otherUser + " to accept";
        }

        notify(Notification.builder()
                .notificationType(NotificationType.ACCEPT_REQUEST)
                .text(text)
                .userId(acceptanceRequest.getAcceptingUser())
                .build());
    }
}

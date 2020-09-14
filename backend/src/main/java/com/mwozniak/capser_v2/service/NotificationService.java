package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.exception.NotificationNotFoundException;
import com.mwozniak.capser_v2.repository.NotificationRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.*;

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
                .seen(false)
                .date(new Date())
                .build());
    }

    public void markSeen(UUID notificationId) throws NotificationNotFoundException {
        Optional<Notification> notificationOptional = notificationRepository.findNotificationById(notificationId);
        if(notificationOptional.isPresent()){
            Notification notification = notificationOptional.get();
            notification.setSeen(true);
            notificationRepository.save(notification);
        } else {
            throw new NotificationNotFoundException("Notification with this id doesn't exist");
        }
    }

    public List<Notification> getNotifications(){
        List<Notification> notifications =  notificationRepository.findNotificationByUserId(SecurityUtils.getUserId());
        notifications.sort(Notification.Comparators.DATE);
        return notifications;
    }
}

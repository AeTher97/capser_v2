package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.exception.NotificationNotFoundException;
import com.mwozniak.capser_v2.repository.NotificationRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notify(Notification notification) {
        notificationRepository.save(notification);
    }


    public void notify(AcceptanceRequest acceptanceRequest, String otherUser) {

        String text;

        if (acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)) {
            text = "Your game with user " + otherUser + " is waiting to be accepted.";
        } else {
            text = "You have a game with user " + otherUser + " to accept.";
        }

        notify(Notification.builder()
                .notificationType(NotificationType.ACCEPT_REQUEST)
                .text(text)
                .userId(acceptanceRequest.getAcceptingUser())
                .seen(false)
                .date(new Date())
                .build());
    }

    public void notifyMultiple(AcceptanceRequest acceptanceRequest, String otherTeam) {

        String text;

        if (acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)) {
            text = "Your game with team " + otherTeam + " is waiting to be accepted.";
        } else {
            text = "You have a game with team " + otherTeam + " to accept.";
        }

        notify(Notification.builder()
                .notificationType(NotificationType.ACCEPT_REQUEST)
                .text(text)
                .userId(acceptanceRequest.getAcceptingUser())
                .seen(false)
                .date(new Date())
                .build());
    }

    public Notification markSeen(UUID notificationId) throws NotificationNotFoundException {
        Optional<Notification> notificationOptional = notificationRepository.findNotificationById(notificationId);
        if (notificationOptional.isPresent()) {
            Notification notification = notificationOptional.get();
            notification.setSeen(true);
            return notificationRepository.save(notification);
        } else {
            throw new NotificationNotFoundException("Notification with this id doesn't exist");
        }
    }

    public List<Notification> getNotifications() {
        List<Notification> notifications = notificationRepository.findNotificationByUserId(SecurityUtils.getUserId());
        notifications.sort(Notification.Comparators.DATE);
        Collections.reverse(notifications);
        if (notifications.size() > 10) {
            int size = notifications.size();
            for (int i = 10; i < notifications.size(); i++) {
                notificationRepository.delete(notifications.get(i));
            }
            log.info("Deleted " + (size - notifications.size()) + " notifications for user" + SecurityUtils.getUserId().toString());
            return notifications.subList(0, 9);
        } else {
            return notifications;
        }
    }
}

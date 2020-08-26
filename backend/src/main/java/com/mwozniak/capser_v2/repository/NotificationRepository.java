package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository  extends JpaRepository<Notification, UUID> {

    List<Notification> findNotificationByUserId(UUID id);
    Optional<Notification> findNotificationById(UUID id);
}

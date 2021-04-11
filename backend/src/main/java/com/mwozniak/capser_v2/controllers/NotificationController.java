package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.exception.NotificationNotFoundException;
import com.mwozniak.capser_v2.service.NotificationService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Log4j
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getAllNotifications() {
        log.info("Getting all notifications");
        return ResponseEntity.ok(notificationService.getNotifications());
    }

    @PutMapping("/seen/{notificationId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> seenNotification(@PathVariable UUID notificationId) throws NotificationNotFoundException {
        log.info("Updating notifications seen status");
        return ResponseEntity.ok(notificationService.markSeen(notificationId));
    }
}

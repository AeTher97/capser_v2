package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.exception.NotificationNotFoundException;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import com.mwozniak.capser_v2.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getAllNotifications(){
        return ResponseEntity.ok(notificationService.getNotifications());
    }

    @PutMapping("/seen/{notificationId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> seenNotification(@PathVariable UUID notificationId) throws NotificationNotFoundException {
        notificationService.markSeen(notificationId);
        return ResponseEntity.ok().build();
    }
}

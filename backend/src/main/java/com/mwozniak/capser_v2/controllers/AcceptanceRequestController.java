package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.service.AcceptanceRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acceptance")
public class AcceptanceRequestController {

    private final AcceptanceRequestService acceptanceRequestService;

    public AcceptanceRequestController(AcceptanceRequestService acceptanceRequestService) {
        this.acceptanceRequestService = acceptanceRequestService;
    }

    @GetMapping
    public ResponseEntity<Object> getAcceptance() {
        return ResponseEntity.ok(acceptanceRequestService.getAcceptanceRequests());
    }
}

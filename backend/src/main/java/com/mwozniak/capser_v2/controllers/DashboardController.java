package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/games")
    public ResponseEntity<Object> getDashboardGames() {
        return ResponseEntity.ok(dashboardService.getDashboardGames());
    }

    @GetMapping("/posts")
    public ResponseEntity<Object> getDashboardPosts() {
        return ResponseEntity.ok(dashboardService.getAllBlogPosts());
    }
}

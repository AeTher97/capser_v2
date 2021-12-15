package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.service.DashboardService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Log4j2
public class DashboardController {


    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/games")
    public ResponseEntity<Object> getDashboardGames() {
        log.info("Getting dashboard games");
        return ResponseEntity.ok(dashboardService.getDashboardGames());
    }

    @GetMapping("/posts")
    public ResponseEntity<Object> getDashboardPosts() {
        log.info("Getting blog posts");
        return ResponseEntity.ok(dashboardService.getAllBlogPosts());
    }
}

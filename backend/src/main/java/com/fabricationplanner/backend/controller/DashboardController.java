package com.fabricationplanner.backend.controller;

import com.fabricationplanner.backend.dto.response.DashboardResponse;
import com.fabricationplanner.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardResponse> getMetrics() {
        return ResponseEntity.ok(dashboardService.getMetrics());
    }
}

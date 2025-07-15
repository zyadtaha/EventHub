package com.eventsystem.controller;

import com.eventsystem.dto.DashboardDto;
import com.eventsystem.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "Dashboard metrics and statistics")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve all dashboard metrics and statistics.")
    public DashboardDto getDashboard() {
        return dashboardService.getDashboardData();
    }
}
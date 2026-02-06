package com.NirajCS.MoneyManager.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.NirajCS.MoneyManager.services.DashboardServices;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor

public class DashboardController {

    private final DashboardServices dashboardServices;
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardData = dashboardServices.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }    
}

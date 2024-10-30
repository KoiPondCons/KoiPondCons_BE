package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/manager")
@CrossOrigin(origins = "*")
public class ManagerController {

    @Autowired
    ManagerService managerService;

    @GetMapping("/dashboard-stats")
    public ResponseEntity getDashboardStats() {
        Map<String, Object> dashboardStats = managerService.getDashboardStats();
        return ResponseEntity.ok(dashboardStats);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity getMonthlyRevenue() {
        return ResponseEntity.ok(managerService.getMonthlyRevenue());
    }
}

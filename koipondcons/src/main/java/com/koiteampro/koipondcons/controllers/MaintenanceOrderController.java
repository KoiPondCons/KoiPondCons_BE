package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.services.MaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class MaintenanceOrderController {

    @Autowired
    private MaintenanceOrderService maintenanceOrderService;

    @PostMapping("/maintenance-orders")
    public ResponseEntity<MaintenanceOrderResponse> create(@RequestBody MaintenanceOrderRequest maintenanceOrderRequest){
        return ResponseEntity.ok(maintenanceOrderService.createMaintenanceOrder(maintenanceOrderRequest));
    }

    @GetMapping("/maintenance-orders/current-constructor")
    public ResponseEntity<MaintenanceOrderResponse> getActiveMaintenanceOrderOfConstructor() {
        return ResponseEntity.ok(maintenanceOrderService.getActiveMaintenanceOrderOfConstructor());
    }

//    @GetMapping("/maintenance-orders/manager")
//    public ResponseEntity getAllConfirmedMaintenanceOrders() {
//
//    }
}

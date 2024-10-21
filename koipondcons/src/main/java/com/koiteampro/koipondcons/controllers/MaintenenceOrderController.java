package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.services.MaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class MaintenenceOrderController {

    @Autowired
    private MaintenanceOrderService maintenanceOrderService;

    @PostMapping("/maintenence")
    public ResponseEntity<MaintenanceOrder> create(@RequestBody MaintenanceOrderRequest maintenanceOrderRequest){
        return ResponseEntity.ok(maintenanceOrderService.createMaintenanceOrder(maintenanceOrderRequest));
    }

}

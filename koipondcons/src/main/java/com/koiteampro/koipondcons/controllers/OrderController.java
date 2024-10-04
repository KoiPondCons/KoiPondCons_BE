package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.models.request.ConstructionOrderStatusRequest;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
import com.koiteampro.koipondcons.services.ConstructionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    ConstructionOrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity create (@RequestBody ConstructionOrderRequest order) {
        ConstructionOrderResponse orderResponse = orderService.createConstructionOrder(order);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity update (@PathVariable int id, @RequestBody ConstructionOrderUpdateRequest order) {
        ConstructionOrderResponse orderResponse = orderService.updateConstructionOrder(id, order);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/orders/{id}/{consultantId}")
    public ResponseEntity setConsulting (@PathVariable long id, @PathVariable long consultantId) {
        orderService.setConsultingToOrder(id,consultantId);
        return ResponseEntity.ok("Set consulting successfully!");
    }

    @GetMapping("/orders/status")
    public ResponseEntity getOrderByStatus (@RequestBody ConstructionOrderStatusRequest status) {
        return ResponseEntity.ok(orderService.getAllConstructionOrdersByStatus(status.getStatus()));
    }

    @GetMapping("/orders")
    public ResponseEntity getAllOrders () {
        return ResponseEntity.ok(orderService.getAllConstructionOrders());
    }

    @GetMapping("/demo")
    public ResponseEntity demo () {
        return ResponseEntity.ok("Okeee");
    }
}

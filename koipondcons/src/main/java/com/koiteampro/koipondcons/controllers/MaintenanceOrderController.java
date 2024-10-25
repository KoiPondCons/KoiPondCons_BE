package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderUpdateRequest;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.services.AuthenticationService;
import com.koiteampro.koipondcons.services.CustomerService;
import com.koiteampro.koipondcons.services.MaintenanceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class MaintenanceOrderController {

    @Autowired
    private MaintenanceOrderService maintenanceOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/maintenance")
    public ResponseEntity<MaintenanceOrderResponse> create(@RequestBody MaintenanceOrderRequest maintenanceOrderRequest){
        return ResponseEntity.ok(maintenanceOrderService.createMaintenanceOrder(maintenanceOrderRequest));
    }

    @GetMapping("/maintenance/customer")
    public ResponseEntity<List<MaintenanceOrderResponse>> getByCreateAtBeforeNowAndByCustomer(){
        return ResponseEntity.ok(maintenanceOrderService.findMaintenanceOrderByCustomerAndBeforeNow(LocalDate.now(), customerService.getCurrentCustomer() ));
    }

    @GetMapping("/maintenance/consultant")
    public ResponseEntity<List<MaintenanceOrderResponse>> getByCreateAtBeforeNowAndByConsultant(){
        return ResponseEntity.ok(maintenanceOrderService.findMaintenanceOrderByConsultantAndBeforeNow(LocalDate.now(), authenticationService.getCurrentAccount()));
    }

    @GetMapping("/maintenance/processed-or-finished")
    public ResponseEntity<List<MaintenanceOrderResponse>> getByCreateAtBeforeNowAndByStatusAndConstructor(@RequestParam MaintenanceOrderStatus status){
        return ResponseEntity.ok(maintenanceOrderService.findMaintenanceOrderBeforeNowAndProcessedOrFinishedAndConstructor(LocalDate.now(), authenticationService.getCurrentAccount(), status));
    }

    @PutMapping("/maintenance/{id}")
    public ResponseEntity<MaintenanceOrderResponse> update(@PathVariable long id, @RequestBody MaintenanceOrderUpdateRequest maintenanceOrderUpdateRequest){
        return ResponseEntity.ok(maintenanceOrderService.updateMaintenanceOrder(id, maintenanceOrderUpdateRequest));
    }

    @GetMapping("/maintenance/current-constructor")
    public ResponseEntity<MaintenanceOrderResponse> getActiveMaintenanceOrderOfConstructor() {
        return ResponseEntity.ok(maintenanceOrderService.getActiveMaintenanceOrderOfConstructor());
    }

    @GetMapping("/maintenance/confirmed-orders")
    public ResponseEntity<List<MaintenanceOrderResponse>> getAllConfirmedMaintenanceOrders() {
        return ResponseEntity.ok(maintenanceOrderService.getAllConfirmedMaintenanceOrders());
    }

    @GetMapping("/maintenance/requested-orders")
    public ResponseEntity<List<MaintenanceOrderResponse>> getAllRequestedMaintenanceOrders() {
        return ResponseEntity.ok(maintenanceOrderService.getAllRequestedMaintenanceOrders());
    }

    @PutMapping("/maintenance/set-consultant/{orderId}")
    public ResponseEntity setConsultantToOrder(@PathVariable long orderId) {
        maintenanceOrderService.setConsultantToOrder(orderId);
        return ResponseEntity.ok("Set consultant to order successfully!");
    }

    @PutMapping("/maintenance/set-constructor/{orderId}/{constructorId}")
    public ResponseEntity setConstructorToOrder(@PathVariable long orderId, @PathVariable long constructorId) {
        maintenanceOrderService.setConstructorToOrder(orderId, constructorId);
        return ResponseEntity.ok("Set constructor to order successfully!");
    }
}

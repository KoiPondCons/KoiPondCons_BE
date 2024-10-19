package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.models.request.ConstructionOrderStatusRequest;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
import com.koiteampro.koipondcons.services.ConstructionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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

    @GetMapping("/orders/{id}")
    public ResponseEntity getOrderById (@PathVariable long id) {
        ConstructionOrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/orders/consultant/{id}")
    public ResponseEntity setConsulting (@PathVariable long id) {
        orderService.setConsultingToOrder(id);
        return ResponseEntity.ok("Set consulting successfully!");
    }

    @GetMapping("/orders/status")
    public ResponseEntity getOrderByStatus (@RequestParam String status) {
        ConstructionOrderStatus orderStatus = ConstructionOrderStatus.valueOf(status);
        return ResponseEntity.ok(orderService.getAllConstructionOrdersByStatus(orderStatus));
    }

    @GetMapping("/orders")
    public ResponseEntity getAllOrders () {
        return ResponseEntity.ok(orderService.getAllConstructionOrders());
    }

    @GetMapping("/orders/customer")
    public ResponseEntity getAllOrdersOfCustomer () {
        return ResponseEntity.ok(orderService.getAllConstructionOrdersOfCustomer());
    }

    @GetMapping("/orders/constructor/current")
    public ResponseEntity getCurrentOrderOfConstructor () {
        return ResponseEntity.ok(orderService.getCurrentOrderOfConstructor());
    }

    @GetMapping("/orders/consultant")
    public ResponseEntity getAllOrdersOfConsultant () {
        return ResponseEntity.ok(orderService.getAllConstructionOrdersOfConsultant());
    }

    @GetMapping("/demo")
    public ResponseEntity demo () {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd(HH:mm:ss)");
        System.out.println("Server Time Zone (old): " + formatter.format(cld.getTime()));
        //////////////////////////////////////////////////////////////////////////////
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneId zoneId2 = ZoneId.of("Asia/Bangkok");
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.now(zoneId2);
        LocalDateTime localDateTime2 = LocalDateTime.now(zoneId2).plusMinutes(15);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        System.out.println("Server Time Zone1 (system)(new): " + dateTimeFormatter.format(localDateTime));
        System.out.println("Server Time Zone2 (usersystem)(new): " + dateTimeFormatter.format(localDateTime1));
        System.out.println("Server Time Zone2 (usersystem)(new)(+15m): " + dateTimeFormatter.format(localDateTime2));
        return ResponseEntity.ok("Okeee");
    }

    @PutMapping("/orders/constructor/{id}/{constructorId}")
    public ResponseEntity<String> setConstructor(@PathVariable long id, @PathVariable long constructorId) {
        orderService.setConstructorToOrder(id,constructorId);
        return ResponseEntity.ok("Set constructor successfully!");
    }

    @GetMapping("/orders/constructor/current/finished")
    public ResponseEntity<List<ConstructionOrder>> getFinishedOrdersByConstructorId(){
        return ResponseEntity.ok(orderService.getFinishedOrdersByCurrentConstructor());
    }
}

package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import com.koiteampro.koipondcons.models.request.ConsOrderPaymentUpdateRequest;
import com.koiteampro.koipondcons.services.ConsOrderPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ConsOrderPaymentController {
    @Autowired
    private ConsOrderPaymentService consOrderPaymentService;

    @PutMapping("/cons-order-payment/{id}/{period}")
    public ResponseEntity<ConsOrderPayment> updateConsOrderPayment(@PathVariable long id, @PathVariable int period, @RequestBody ConsOrderPaymentUpdateRequest consOrderPaymentRequest) {
        return ResponseEntity.ok(consOrderPaymentService.updateConsOrderPayment(id,period, consOrderPaymentRequest.isPaid(), consOrderPaymentRequest.getPaymentMethod()));
    }

    @GetMapping("cons-order-payment/demo")
    public ResponseEntity<List<ConsOrderPayment>> getConsOrderPaymentDemo(@RequestParam long comboId, @RequestParam float pondVolume, @RequestParam boolean designed) {
        return ResponseEntity.ok(consOrderPaymentService.getDemoConsOrderPayments(comboId, pondVolume, designed));
    }
}

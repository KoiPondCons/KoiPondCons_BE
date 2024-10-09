package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.PromotionRequest;
import com.koiteampro.koipondcons.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @PostMapping("/promotions")
    public ResponseEntity createPromotion(@RequestBody PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.createPromotion(promotionRequest));
    }

    @GetMapping("/promotions")
    public ResponseEntity getPromotions() {
        return ResponseEntity.ok(promotionService.getPromotions());
    }

    @GetMapping("/promotions/customer/{id}")
    public ResponseEntity getCustomerPromotionAvailable(@PathVariable long id) {
        return ResponseEntity.ok(promotionService.getPromotionsByCustomerAvailableForCustomer(id));
    }

    @GetMapping("/promotions/{id}")
    public ResponseEntity getPromotion(@PathVariable long id) {
        return ResponseEntity.ok(promotionService.getPromotionById(id));
    }

    @PutMapping("/promotions/{id}")
    public ResponseEntity updatePromotion(@PathVariable long id, @RequestBody PromotionRequest promotionRequest) {
        return ResponseEntity.ok(promotionService.updatePromotion(id, promotionRequest));
    }
}

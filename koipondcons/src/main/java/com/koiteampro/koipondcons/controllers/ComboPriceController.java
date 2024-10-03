package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.ComboPriceRequest;
import com.koiteampro.koipondcons.services.ComboPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ComboPriceController {

    @Autowired
    private ComboPriceService comboPriceService;

    @PostMapping("/comboprices")
    public ResponseEntity addComboPrices(@RequestBody ComboPriceRequest comboPriceRequest) {
        return ResponseEntity.ok(comboPriceService.addComboPrice(comboPriceRequest));
    }

    @GetMapping("/comboprices/combo/{comboId}")
    public ResponseEntity getComboPricesByComboId(@PathVariable Long comboId) {
        return ResponseEntity.ok(comboPriceService.getComboPricesByComboId(comboId));
    }

    @GetMapping("/comboprices/{id}")
    public ResponseEntity getComboPricesById(@PathVariable Long id) {
        return ResponseEntity.ok(comboPriceService.getComboPriceById(id));
    }

    @PutMapping("/comboprices/{id}")
    public ResponseEntity updateComboPrices(@PathVariable Long id, @RequestBody ComboPriceRequest comboPriceRequest) {
        return ResponseEntity.ok(comboPriceService.updateComboPrice(id, comboPriceRequest));
    }
}

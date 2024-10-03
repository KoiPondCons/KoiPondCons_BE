package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.QuotationRequest;
import com.koiteampro.koipondcons.services.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @PutMapping("/quotations/promo/{id}/{promotionId}")
    public ResponseEntity addPromotionToQuotation(@PathVariable long id, @PathVariable long promotionId) {
        quotationService.addPromotionToQuotation(id, promotionId);
        return ResponseEntity.ok("Add Promotion to Quotation Successfully!");
    }

    @DeleteMapping("/quotations/promo/{id}/{promotionId}")
    public ResponseEntity removePromotionFromQuotation(@PathVariable long id, @PathVariable long promotionId) {
        quotationService.removePromotionFromQuotation(id, promotionId);
        return ResponseEntity.ok("Remove Promotion from Quotation Successfully!");
    }

    @PutMapping("/quotation/{id}")
    public ResponseEntity updateQuotation(@PathVariable long id, @RequestBody QuotationRequest quotationRequest) {
        return ResponseEntity.ok(quotationService.updateQuotation(id, quotationRequest));
    }


}

package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.ComboConstructionItemRequest;
import com.koiteampro.koipondcons.services.ComboConstructionItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ComboConstructionItemController {

    @Autowired
    private ComboConstructionItemService comboConstructionItemService;

    @PostMapping("/comboconstructionitems")
    public ResponseEntity addComboConstructionItems(@RequestBody ComboConstructionItemRequest comboConstructionItemRequest) {
        return ResponseEntity.ok(comboConstructionItemService.addComboConstructionItem(comboConstructionItemRequest));
    }

    @GetMapping("/comboconstructionitems/combo/{comboId}")
    public ResponseEntity getComboConstructionItemsByComboId(@PathVariable Long comboId) {
        return ResponseEntity.ok(comboConstructionItemService.getAllComboConstructionItemsByComboId(comboId));
    }

    @GetMapping("/comboconstructionitems/{id}")
    public ResponseEntity getComboConstructionItemsById(@PathVariable Long id) {
        return ResponseEntity.ok(comboConstructionItemService.getComboConstructionItemById(id));
    }

    @PutMapping("/comboconstructionitems/{id}")
    public ResponseEntity updateComboConstructionItems(@PathVariable Long id, @RequestBody ComboConstructionItemRequest comboConstructionItemRequest) {
        return ResponseEntity.ok(comboConstructionItemService.updateComboConstructionItem(id, comboConstructionItemRequest));
    }
}

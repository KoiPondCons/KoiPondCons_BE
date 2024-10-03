package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.ComboRequest;
import com.koiteampro.koipondcons.services.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ComboController {

    @Autowired
    private ComboService comboService;

    ///////////////// API for combos /////////////////

    @PostMapping("/combos")
    public ResponseEntity createCombo(@RequestBody ComboRequest comboRequest) {
        return ResponseEntity.ok(comboService.createCombo(comboRequest));
    }

    @GetMapping("/combos")
    public ResponseEntity getAllCombos() {
        return ResponseEntity.ok(comboService.getAllCombos());
    }

    @PutMapping("/combos/{id}")
    public ResponseEntity updateCombo(@PathVariable Long id, @RequestBody ComboRequest comboRequest) {
        return ResponseEntity.ok(comboService.updateCombo(id, comboRequest));
    }

    @DeleteMapping("/combos/{id}")
    public ResponseEntity deleteCombo(@PathVariable Long id) {
        comboService.deleteCombo(id);
        return ResponseEntity.ok("Deleted combo id " + id);
    }
}

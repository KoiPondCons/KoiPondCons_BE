package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.services.DesignDrawingService;
import com.koiteampro.koipondcons.services.StaffConstructionDetailService;
import com.koiteampro.koipondcons.services.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountController {
    @Autowired
    StaffService staffService;

    @Autowired
    DesignDrawingService designDrawingService;

    @GetMapping("/free-constructors")
    public ResponseEntity getAllFreeConstructors() {
        List<AccountResponse> accounts = staffService.getALlFreeConstructor();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/free-designers")
    public ResponseEntity getAllFreeDesigners() {
        List<AccountResponse> accounts = designDrawingService.getAllFreeDesigners();
        return ResponseEntity.ok(accounts);
    }
}

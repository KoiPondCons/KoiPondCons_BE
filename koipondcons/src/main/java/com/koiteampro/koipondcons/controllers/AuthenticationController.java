package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.AccountResponse;
import com.koiteampro.koipondcons.models.LoginRequest;
import com.koiteampro.koipondcons.models.RegisterRequest;
import com.koiteampro.koipondcons.services.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
@SecurityRequirement(name = "api")  //yêu cầu xác thực tài khoản để truy cập API
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }
}

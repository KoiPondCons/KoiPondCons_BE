package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.models.request.UpdateFCMRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
//@SecurityRequirement(name = "api")  //yêu cầu xác thực tài khoản để truy cập API
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("register")
    public ResponseEntity<AccountResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity<AccountResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/update-fcm-token")
    public ResponseEntity<String> updateFcmToken(@RequestBody UpdateFCMRequest updateFCMRequest) {
        // Lấy tài khoản người dùng hiện tại từ SecurityContext
        authenticationService.updateFCM(updateFCMRequest);
        return ResponseEntity.ok("FCM Token được cập nhật thành công");
    }

}

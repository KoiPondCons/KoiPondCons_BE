package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.models.request.SetRoleRequest;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.services.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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

    @PutMapping("{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        AccountResponse accountResponse = null;

            accountResponse = authenticationService.updateAccount(id, updateAccountRequest);

        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {

        boolean isDeleted = authenticationService.deleteAccount(id);

            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
            }


        return ResponseEntity.ok("Xóa rùi");
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(authenticationService.getAllAccounts());
    }

    @GetMapping("id/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") long id) {
        return ResponseEntity.ok(authenticationService.getAccountById(id));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<AccountResponse>> findAccountByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(authenticationService.findAccountByName(name));
    }

    @GetMapping("role/{role}")
    public ResponseEntity<List<AccountResponse>> findAccountByRole(@PathVariable("role") Role role) {
        return ResponseEntity.ok(authenticationService.getAccountByRole(role));
    }

    @PutMapping("role/{id}")
    public ResponseEntity<String> setRole(@PathVariable("id") long id, @RequestBody SetRoleRequest setRoleRequest){
        try {

            Role enumRole = setRoleRequest.getRole();
            boolean isSetRole = authenticationService.setRole(id, enumRole);
            if (isSetRole) {
                return ResponseEntity.ok("Set OK");
            } else {
                return ResponseEntity.ok("Set Error");
            }
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }
    }


}

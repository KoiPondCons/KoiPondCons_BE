package com.koiteampro.koipondcons.controllers;

import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.models.request.SetRoleRequest;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.services.AccountService;
import com.koiteampro.koipondcons.services.DesignDrawingService;
import com.koiteampro.koipondcons.services.StaffService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@CrossOrigin("*")
public class AccountController {
    @Autowired
    StaffService staffService;

    @Autowired
    DesignDrawingService designDrawingService;

    @Autowired
    AccountService accountService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/current")
    public ResponseEntity getCurrentAccount() {
        return ResponseEntity.ok(modelMapper.map(accountService.getCurrentAccount(), AccountResponse.class));
    }

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

    @PutMapping("{id}")
    public ResponseEntity<AccountResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        AccountResponse accountResponse = null;

        accountResponse = accountService.updateAccount(id, updateAccountRequest);

        return ResponseEntity.ok(accountResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {

        boolean isDeleted = accountService.deleteAccount(id);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tài khoản không tồn tại");
        }


        return ResponseEntity.ok("Xóa rùi");
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("id/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<List<AccountResponse>> findAccountByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(accountService.findAccountByName(name));
    }

    @GetMapping("role/{role}")
    public ResponseEntity<List<AccountResponse>> findAccountByRole(@PathVariable("role") Role role) {
        return ResponseEntity.ok(accountService.getAccountByRole(role));
    }

    @PutMapping("role/{id}")
    public ResponseEntity<String> setRole(@PathVariable("id") long id, @RequestBody SetRoleRequest setRoleRequest){
        try {

            Role enumRole = setRoleRequest.getRole();
            boolean isSetRole = accountService.setRole(id, enumRole);
            if (isSetRole) {
                return ResponseEntity.ok("Set OK");
            } else {
                return ResponseEntity.ok("Set Error");
            }
        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        }
    }
    @GetMapping("/role/staff")
    public ResponseEntity<List<AccountResponse>> getAllStaff(){
        return ResponseEntity.ok(accountService.getAllStaff());
    }
}

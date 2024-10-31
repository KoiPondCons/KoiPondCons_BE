package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }

    public AccountResponse updateAccount(long id, UpdateAccountRequest updateAccountRequest ) {
        Account account = accountRepository.findAccountById(id);


        if (account == null) {
            throw new EntityNotFoundException("Id không tồn tại");

        }

        modelMapper.map(updateAccountRequest, account);

        if(account.isEnabled()) {
            if (updateAccountRequest.getName() != null) {
                account.setName(updateAccountRequest.getName());
            }
            if (updateAccountRequest.getEmail() != null) {
                account.setEmail(updateAccountRequest.getEmail());
            }
            if(updateAccountRequest.getAddress() != null){
                account.setAddress(updateAccountRequest.getAddress());
            }
            if (updateAccountRequest.getAvatar() != null) {
                account.setAvatar(updateAccountRequest.getAvatar());
            }
            if (updateAccountRequest.getPhone() != null) {
                account.setPhone(updateAccountRequest.getPhone());
            }

        }
        accountRepository.save(account);

        return modelMapper.map(account, AccountResponse.class);

    }

    public boolean deleteAccount(long id) {

        Account account = accountRepository.findAccountById(id);

        if (account == null) {
            return false;
        }
        //try{
        account.setEnabled(false);
        accountRepository.save(account);
//        }catch(Exception e) {
//            throw new UnauthorizeException("Không có quyền xóa");
//        }


        return true;
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> modelMapper.map(account, AccountResponse.class)).collect(Collectors.toList());
    }

    public AccountResponse getAccountById(long id){
        Account account = accountRepository.findAccountById(id);
        try{
            return modelMapper.map(account, AccountResponse.class);
        }catch(Exception e){
            throw new EntityNotFoundException("Id không tồn tại");
        }
    }

    public List<AccountResponse> getAccountByRole(Role role){
        List<Account> accounts = accountRepository.findAccountByRoleAndIsEnabledTrue(role);
        return accounts.stream().map(account -> modelMapper.map(account, AccountResponse.class)).collect(Collectors.toList());
    }

    public List<AccountResponse> findAccountByName(String name){
        List<AccountResponse> accountResponseList = new ArrayList<>();
        List<AccountResponse> allAccounts = this.getAllAccounts();
        for(AccountResponse accountResponse : allAccounts) {
            if (accountResponse.getName().toLowerCase().contains(name)) {
                accountResponseList.add(accountResponse);
            }
        }
        return accountResponseList;
    }

    public boolean setRole(long id, Role role) {
        Account account = accountRepository.findAccountById(id);
        if (account == null) {
            return false;
        }
        account.setRole(role);
        accountRepository.save(account);
        return true;
    }

    public AccountResponse getAccountResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setName(account.getName());
        accountResponse.setRole(account.getRole());
        accountResponse.setAvatar(account.getAvatar());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setAddress(account.getAddress());
        accountResponse.setPhone(account.getPhone());
        accountResponse.setDateCreate(account.getDateCreate());
        return accountResponse;
    }

    public List<AccountResponse> getAllStaff(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.CUSTOMER);
        roles.add(Role.MANAGER);
        List<Account> accounts = accountRepository.findByRoleNotInAndIsEnabledTrue(roles);
        return accounts.stream().map(account -> modelMapper.map(account, AccountResponse.class)).collect(Collectors.toList());
    }
}


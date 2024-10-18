package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.DuplicateEntity;
import com.koiteampro.koipondcons.models.response.EmailDetail;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Service
public class AuthenticationService implements UserDetailsService {
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


    public AccountResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        try {
            //mã hóa password
            String originPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originPassword));
            account.setDateCreate(LocalDate.now());
            Account newAccount = accountRepository.save(account);

            if(registerRequest.getRole() == Role.CUSTOMER) {
                Customer customer = new Customer();
                customer.setAccount(newAccount);
                customer.setTotal_points(0);
                customerRepository.save(customer);
            }

            //sau khi đăng kí thành công, gửi mail về cho người dùng
//            EmailDetail emailDetail = new EmailDetail();
//            emailDetail.setReceiver(newAccount);
//            emailDetail.setSubject("Welcome to B-Learning, ");
//            emailDetail.setLink("https://www.google.com/");
//            emailService.sendEmail(emailDetail);

            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (Exception e) {
            if (e.getMessage().contains(account.getEmail())) {
                throw new DuplicateEntity("Email đã được đăng ký, vui lòng sử dụng email khác!");
            }
            else {
                throw new DuplicateEntity(e.getMessage());
            }
        }
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        } catch (Exception e) {
            throw new EntityNotFoundException("Mật khẩu không đúng hoặc email không tồn tại!");
        }
    }

    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepository.findAccountByEmailAndIsEnabledTrue(email);
    }


    public AccountResponse updateAccount(long id, UpdateAccountRequest updateAccountRequest ) {
        Account account = accountRepository.findAccountById(id);

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

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
        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);

        return accountResponse;

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
        List<Account> accounts = accountRepository.findByRoleNot(Role.CUSTOMER);
        return accounts.stream().map(account -> modelMapper.map(account, AccountResponse.class)).collect(Collectors.toList());
    }
}

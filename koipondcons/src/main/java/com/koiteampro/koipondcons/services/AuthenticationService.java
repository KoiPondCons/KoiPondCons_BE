package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.DuplicateEntity;
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
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    @Autowired
    CustomerRepository customerRepository;

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


}

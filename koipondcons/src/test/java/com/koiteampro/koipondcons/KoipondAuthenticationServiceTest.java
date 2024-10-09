package com.koiteampro.koipondcons;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.DuplicateEntity;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.services.AuthenticationService;
import com.koiteampro.koipondcons.services.EmailService;
import com.koiteampro.koipondcons.services.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class KoipondAuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Account existingAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Customer_Success(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Hoa Customer");
        registerRequest.setEmail("hoa@gmail.com");
        registerRequest.setPhone("0896671154");
        registerRequest.setPassword("123456");
        registerRequest.setRole(Role.CUSTOMER);

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword("123456");

        Account savedAccount = new Account();
        savedAccount.setEmail("hoa@gmail.com");
        savedAccount.setPassword("passwordEncoded");

        AccountResponse expectedAccountResponse = new AccountResponse();
        expectedAccountResponse.setEmail("hoa@gmail.com");

        Mockito.when(modelMapper.map(registerRequest, Account.class)).thenReturn(account);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("passwordEncoded");
        Mockito.when(accountRepository.save(account)).thenReturn(savedAccount);

        Mockito.when(modelMapper.map(savedAccount, AccountResponse.class)).thenReturn(expectedAccountResponse);

        AccountResponse accountResponse = authenticationService.register(registerRequest);
        if(accountResponse == null){
            System.out.println("accountResponse is null");
            return;
        }

        Mockito.verify(accountRepository).save(account);
        assertNotNull(accountResponse);
        assertEquals("hoa@gmail.com", accountResponse.getEmail());

    }

    @Test
    public void testRegister_Customer_EmailExisted(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Hoa Customer");
        registerRequest.setEmail("hoa@gmail.com");
        registerRequest.setPhone("0896671154");
        registerRequest.setPassword("123456");
        registerRequest.setRole(Role.CUSTOMER);

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword(registerRequest.getPassword());

        Mockito.when(modelMapper.map(registerRequest, Account.class)).thenReturn(account);
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("passwordEncoded");
        Mockito.when(accountRepository.save(account)).thenThrow(new RuntimeException("hoa@gmail.com"));

        Exception exception = assertThrows(DuplicateEntity.class, () ->{
            authenticationService.register(registerRequest);
        });

        assertTrue(exception.getMessage().contains("Email đã được đăng ký, vui lòng sử dụng email khác!"));
    }

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("hoa@gmail.com");
        loginRequest.setPassword("123456");

        Authentication authentication = Mockito.mock(Authentication.class);

        Account account = new Account();
        account.setEmail("hoa@gmail.com");
        account.setPassword("123456");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(account);
        Mockito.when(tokenService.generateToken(account)).thenReturn("jwt-token");

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail("hoa@gmail.com");
        Mockito.when(modelMapper.map(account, AccountResponse.class)).thenReturn(accountResponse);

        AccountResponse result = authenticationService.login(loginRequest);

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("hoa@gmail.com", result.getEmail());
        assertEquals("jwt-token", result.getToken());

    }

    @Test
    public void testLogin_Failure() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@gmail.com");
        loginRequest.setPassword("123456");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        Exception exception = assertThrows(EntityNotFoundException.class, () -> authenticationService.login(loginRequest));

        assertEquals("Mật khẩu không đúng hoặc email không tồn tại!", exception.getMessage());
    }

    @Test
    public void testUpdateAccount_Success() {
        long id = 1L;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setName("Hoa1");
        updateAccountRequest.setEmail("hoa1@gmail.com");
        updateAccountRequest.setAddress("address");
        updateAccountRequest.setAvatar("url");
        updateAccountRequest.setPhone("0904511170");

        existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setEnabled(true);

        Mockito.when(accountRepository.findAccountById(1L)).thenReturn(existingAccount);
        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        Mockito.when(modelMapper.map(updateAccountRequest, Account.class)).thenReturn(existingAccount);

        AccountResponse accountResponse = new AccountResponse();
        Mockito.when(modelMapper.map(existingAccount, AccountResponse.class)).thenReturn(accountResponse);

        AccountResponse result = authenticationService.updateAccount(id, updateAccountRequest);

        assertNotNull(accountResponse);
        assertEquals(accountResponse, result);
        Mockito.verify(accountRepository).save(existingAccount);
        assertEquals("Hoa1", existingAccount.getName());
        assertEquals("hoa1@gmail.com", existingAccount.getEmail());
        assertEquals("address", existingAccount.getAddress());
        assertEquals("url", existingAccount.getAvatar());
        assertEquals("0904511170", existingAccount.getPhone());

    }

    @Test
    public void testUpdateAccount_NotFoundId() {
        long id = 1L;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();

        Mockito.when(accountRepository.findAccountById(id)).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> authenticationService.updateAccount(id, updateAccountRequest));
        assertEquals("Id không tồn tại", exception.getMessage());
    }

    @Test
    public void testUpdateAccount_NotEnabled() {
        long id = 1L;
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setName("Hoa1");

        existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setName("Hoa");
        existingAccount.setEnabled(false);

        Mockito.when(accountRepository.findAccountById(id)).thenReturn(existingAccount);
        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(existingAccount);
        Mockito.when(modelMapper.map(updateAccountRequest, Account.class)).thenReturn(existingAccount);

        AccountResponse accountResponse = new AccountResponse();
        Mockito.when(modelMapper.map(existingAccount, AccountResponse.class)).thenReturn(accountResponse);

        AccountResponse result = authenticationService.updateAccount(id, updateAccountRequest);

        assertNotNull(accountResponse);
        assertEquals(accountResponse, result);
        Mockito.verify(accountRepository).save(existingAccount);
        assertNotEquals("Hoa1", result.getName());
    }
}

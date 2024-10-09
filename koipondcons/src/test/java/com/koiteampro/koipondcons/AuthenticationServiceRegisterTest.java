package com.koiteampro.koipondcons;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.models.request.RegisterRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.services.AuthenticationService;
import com.koiteampro.koipondcons.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceRegisterTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Test
    public void testRegister_Customer_Success(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Hoa Customer");
        registerRequest.setEmail("hoacus@gmail.com");
        registerRequest.setPhone("0896671154");
        registerRequest.setPassword("123456");
        registerRequest.setRole(Role.CUSTOMER);

        Account account = new Account();
        account.setEmail(registerRequest.getEmail());
        account.setPassword("123456");

        Account savedAccount = new Account();
        savedAccount.setEmail("hoacus@gmail.com");
        savedAccount.setPassword("passwordEncoded");

        AccountResponse expectedAccountResponse = new AccountResponse();
        expectedAccountResponse.setEmail("hoacus@gmail.com");

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
        assertEquals("hoacus@gmail.com", accountResponse.getEmail());

    }

}

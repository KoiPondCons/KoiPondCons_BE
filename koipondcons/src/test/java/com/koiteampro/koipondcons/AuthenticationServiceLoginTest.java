package com.koiteampro.koipondcons;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.models.request.LoginRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.services.AuthenticationService;
import com.koiteampro.koipondcons.services.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceLoginTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("hoacus@gmail.com");
        loginRequest.setPassword("123456");

        Authentication authentication = Mockito.mock(Authentication.class);

        Account account = new Account();
        account.setEmail("hoacus@gmail.com");
        account.setPassword("123456");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(account);
        Mockito.when(tokenService.generateToken(account)).thenReturn("jwt-token");

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setEmail("hoacus@gmail.com");
        Mockito.when(modelMapper.map(account, AccountResponse.class)).thenReturn(accountResponse);

        AccountResponse result = authenticationService.login(loginRequest);

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("hoacus@gmail.com", result.getEmail());
        assertEquals("jwt-token", result.getToken());

    }
}

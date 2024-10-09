package com.koiteampro.koipondcons.AuthenticationServiceTest;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.models.request.UpdateAccountRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.services.AuthenticationService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UpdateTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ModelMapper modelMapper;

    private Account existingAccount;

    @BeforeEach
    public void setUp() {
//        existingAccount = new Account();
//        existingAccount.setId(1L);
//        existingAccount.setEmail("hoa@gmail.com");
//        existingAccount.setPassword("123456");
//        existingAccount.setPhone("0896671154");
//        existingAccount.setName("Hoa");
//        existingAccount.setEnabled(true);
        MockitoAnnotations.openMocks(this);
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

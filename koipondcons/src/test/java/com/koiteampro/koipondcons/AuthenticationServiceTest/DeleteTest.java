package com.koiteampro.koipondcons.AuthenticationServiceTest;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DeleteTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private AccountRepository accountRepository;

    private Account existingAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testDelete_Success(){
        long id = 1L;
        existingAccount = new Account();
        existingAccount.setId(id);
        existingAccount.setEnabled(true);

        Mockito.when(accountRepository.findAccountById(id)).thenReturn(existingAccount);

        boolean result = authenticationService.deleteAccount(id);

        assertTrue(result);
        assertFalse(existingAccount.isEnabled());
        Mockito.verify(accountRepository).save(existingAccount);

    }
}

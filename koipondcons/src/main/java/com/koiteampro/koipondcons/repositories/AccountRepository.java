package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmailAndIsEnabledTrue(String email);
    Account findAccountById(long id);

}

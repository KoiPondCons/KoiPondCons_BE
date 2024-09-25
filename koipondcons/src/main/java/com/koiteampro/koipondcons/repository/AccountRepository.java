package com.koiteampro.koipondcons.repository;

import com.koiteampro.koipondcons.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmailAndIsEnabledTrue(String email);
    Account findAccountById(long id);
}

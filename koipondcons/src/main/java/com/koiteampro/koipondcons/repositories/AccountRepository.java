package com.koiteampro.koipondcons.repositories;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmailAndIsEnabledTrue(String email);
    Account findAccountById(long id);
    List<Account> findAccountsByIsEnabledTrue();

    List<Account> findAccountByRoleAndIsEnabledTrue(Role role);
    List<Account> findByIdNotIn(List<Long> ids);
}

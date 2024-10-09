package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffConstructionDetailService {
    @Autowired
    private StaffConstructionDetailRepository staffConstructionDetailRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<StaffConstructionDetail> findStaffConstructionDetailByConstructionOrderId(long constructionOrderId) {
        return  staffConstructionDetailRepository.findAllByConstructionOrderId(constructionOrderId);
    }

    public List<Account> getAllFreeConstructors() {
        List<Account> freeConstructors = new ArrayList<>();
        try {
            List<Long> accountIds = new ArrayList<>();
            try {
                accountIds = staffConstructionDetailRepository.findConstructorAccountIdByIsFinishedFalse();
            } catch (Exception e) {
                accountIds = null;
            }
            if (accountIds == null || accountIds.isEmpty()) {
                return accountRepository.findAccountByRoleAndIsEnabledTrue(Role.CONSTRUCTOR);
            }
            freeConstructors = accountRepository.findByIdNotIn(accountIds);
            return freeConstructors;
        } catch (Exception e) {
            throw new NotFoundException("Staff not found!");
        }
    }
}

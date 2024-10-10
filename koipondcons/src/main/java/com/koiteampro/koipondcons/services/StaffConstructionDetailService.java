package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.response.AccountResponse;
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

    @Autowired
    private AuthenticationService authenticationService;

    public List<StaffConstructionDetail> findStaffConstructionDetailByConstructionOrderId(long constructionOrderId) {
        return  staffConstructionDetailRepository.findAllByConstructionOrderId(constructionOrderId);
    }

    public List<AccountResponse> getAllFreeConstructors() {
        List<Account> freeConstructors = new ArrayList<>();
        List<Long> accountIds = new ArrayList<>();

        try {
            accountIds = staffConstructionDetailRepository.findConstructorAccountIdByIsFinishedFalse();
        } catch (Exception e) {
            accountIds = null;
        }

        try {
            if (accountIds == null || accountIds.isEmpty()) {
                freeConstructors = accountRepository.findAccountByRoleAndIsEnabledTrue(Role.CONSTRUCTOR);
            } else {
                freeConstructors = accountRepository.findByIdNotIn(accountIds);
            }
        } catch (Exception e) {
            throw new NotFoundException("Staff not found!");
        }

        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : freeConstructors) {
            AccountResponse accountResponse = authenticationService.getAccountResponse(account);
            accountResponses.add(accountResponse);
        }

        return accountResponses;
    }
}

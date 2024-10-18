package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.StaffConstructionDetailUpdateRequest;
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

    public Account getConstructorOfConstructionOrder(long constructionOrderId) {
        List<StaffConstructionDetail> staffConstructionDetailList = staffConstructionDetailRepository.findAllByConstructionOrderId(constructionOrderId);

        if (staffConstructionDetailList != null && !staffConstructionDetailList.isEmpty()) {
            return staffConstructionDetailList.getFirst().getConstructorAccount();
        } else {
            return null;
        }
    }

    public double getProgressByConstructionOrder(long orderId) {
        try {
            String result = String.format("%.2f", staffConstructionDetailRepository.countByConstructionOrderIdAndIsFinishedTrue(orderId) * 1.0
                    / staffConstructionDetailRepository.countByConstructionOrderId(orderId))
            return Double.parseDouble(result)*100;
        } catch (Exception e) {
            throw new NotFoundException("Order not found!");
        }
    }

    public StaffConstructionDetail updateStaffConstructionDetail(long detailId, StaffConstructionDetailUpdateRequest staffConstructionDetailUpdateRequest) {
        StaffConstructionDetail oldDetail = staffConstructionDetailRepository.findById(detailId).orElse(null);
        if (oldDetail != null) {
            oldDetail.setDateStart(staffConstructionDetailUpdateRequest.getDateStart());
            oldDetail.setDateEnd(staffConstructionDetailUpdateRequest.getDateEnd());
            oldDetail.setFinished(staffConstructionDetailUpdateRequest.isFinished());
            return staffConstructionDetailRepository.save(oldDetail);
        }
        else {
            throw new NotFoundException("Work not found!");
        }
    }
}

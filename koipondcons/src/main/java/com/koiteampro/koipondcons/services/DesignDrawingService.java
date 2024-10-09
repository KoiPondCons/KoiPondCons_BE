package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.DesignDrawing;
import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.DesignDrawingRequest;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.DesignDrawingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DesignDrawingService {

    @Autowired
    private DesignDrawingRepository designDrawingRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    public void updateDesignDrawing(long id, DesignDrawingRequest designDrawingRequest) {
        Optional<DesignDrawing> designDrawingOptional = designDrawingRepository.findById(id);

        if (designDrawingOptional.isPresent()) {
            DesignDrawing designDrawing = designDrawingOptional.get();
            designDrawing.setDesignerAccount(designDrawingRequest.getDesignerAccount());
            designDrawing.setDesignFile(designDrawingRequest.getDesignFile());
            designDrawing.setStatus(designDrawingRequest.getStatus());
            designDrawingRepository.save(designDrawing);
        } else {
            throw new NotFoundException("DesignDrawing with id " + id + " not found");
        }
    }

    public DesignDrawing getDesignDrawing(long id) {
        Optional<DesignDrawing> designDrawingOptional = designDrawingRepository.findById(id);

        if (designDrawingOptional.isPresent()) {
            return designDrawingOptional.get();
        } else {
            throw new NotFoundException("DesignDrawing with id " + id + " not found");
        }
    }

    public List<DesignDrawing> getAllDesignDrawings() {
        return designDrawingRepository.findAll();
    }

    public List<DesignDrawing> getAllDesignOfDesigner() {
        Account currentAccount = authenticationService.getCurrentAccount();
        return designDrawingRepository.findAllByDesignerAccountId(currentAccount.getId());
    }

    public List<Account> getAllFreeDesigners() {
        List<Account> accounts = new ArrayList<>();
        try {
            List<Long> accountIds = new ArrayList<>();
            try {
                accountIds = designDrawingRepository.findDesignerAccountIdByStatusNotLike("%" + DesignDrawingStatus.CUSTOMER_CONFIRMED.toString() + "%");
            } catch (Exception e) {
                accountIds = null;
            }
            if (accountIds == null || accountIds.isEmpty()) {
                return accountRepository.findAccountByRoleAndIsEnabledTrue(Role.DESIGNER);
            }
            accounts = accountRepository.findByIdNotIn(accountIds);
            return accounts;
        } catch (Exception e) {
            throw new NotFoundException("Staff not found!");
        }
    }
}

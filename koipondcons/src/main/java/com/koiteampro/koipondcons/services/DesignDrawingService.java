package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.ConsOrderPayment;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.DesignDrawing;
import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.DesignDrawingRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.response.DesignDrawingResponse;
import com.koiteampro.koipondcons.models.response.OrderCustomerResponse;
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

    @Autowired
    ConsOrderPaymentService consOrderPaymentService;

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

    public DesignDrawingResponse getDesignDrawing(long id) {
        Optional<DesignDrawing> designDrawingOptional = designDrawingRepository.findById(id);

        if (designDrawingOptional.isPresent()) {
            DesignDrawing designDrawing = designDrawingOptional.get();
            return getDesignDrawingResponse(designDrawing);
        } else {
            throw new NotFoundException("DesignDrawing with id " + id + " not found");
        }
    }

    public List<DesignDrawingResponse> getAllDesignDrawings() {
        List<DesignDrawing> designDrawings = designDrawingRepository.findAll();
        List<DesignDrawingResponse> designDrawingResponses = new ArrayList<>();
        for (DesignDrawing designDrawing : designDrawings) {
            DesignDrawingResponse designDrawingResponse = getDesignDrawingResponse(designDrawing);
            designDrawingResponses.add(designDrawingResponse);
        }
        return designDrawingResponses;
    }

    public List<DesignDrawingResponse> getAllDesignOfDesigner() {
        Account currentAccount = authenticationService.getCurrentAccount();
        List<DesignDrawing> designDrawings = designDrawingRepository.findAllByDesignerAccountId(currentAccount.getId());
        List<DesignDrawingResponse> designDrawingResponses = new ArrayList<>();
        for (DesignDrawing designDrawing : designDrawings) {
            DesignDrawingResponse designDrawingResponse = getDesignDrawingResponse(designDrawing);
            designDrawingResponses.add(designDrawingResponse);
        }
        return designDrawingResponses;
    }

    public List<AccountResponse> getAllFreeDesigners() {
        List<Account> accounts = new ArrayList<>();
        List<Long> accountIds = new ArrayList<>();

        try {
            accountIds = designDrawingRepository.findDesignerAccountIdByStatusNotLike("%" + DesignDrawingStatus.CUSTOMER_CONFIRMED + "%");
        } catch (Exception e) {
            accountIds = null;
        }

        try {
            if (accountIds == null || accountIds.isEmpty()) {
                accounts = accountRepository.findAccountByRoleAndIsEnabledTrue(Role.DESIGNER);
            } else {
                accounts = accountRepository.findByIdNotInAndRoleLike(accountIds, Role.DESIGNER);
            }
        } catch (Exception e) {
            throw new NotFoundException("Staff not found!");
        }

        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            AccountResponse accountResponse = authenticationService.getAccountResponse(account);
            accountResponses.add(accountResponse);
        }

        return accountResponses;
    }

    public DesignDrawingResponse getDesignDrawingResponse(DesignDrawing designDrawing) {
        DesignDrawingResponse designDrawingResponse = new DesignDrawingResponse();
        designDrawingResponse.setId(designDrawing.getId());
        OrderCustomerResponse orderCustomerResponse = new OrderCustomerResponse();
        orderCustomerResponse.setOrderId(designDrawing.getConstructionOrder().getId());
        orderCustomerResponse.setCustomerName(designDrawing.getConstructionOrder().getCustomerName());
        orderCustomerResponse.setCustomerPhone(designDrawing.getConstructionOrder().getCustomerPhone());
        orderCustomerResponse.setPondAddress(designDrawing.getConstructionOrder().getPondAddress());
        designDrawingResponse.setOrderCustomerResponse(orderCustomerResponse);
        designDrawingResponse.setDesignerAccount(designDrawing.getDesignerAccount());
        designDrawingResponse.setDesignFile(designDrawing.getDesignFile());
        designDrawingResponse.setStatus(designDrawing.getStatus());
        designDrawingResponse.setStatusDescription(designDrawing.getStatus().getDescription());
        return designDrawingResponse;
    }
}

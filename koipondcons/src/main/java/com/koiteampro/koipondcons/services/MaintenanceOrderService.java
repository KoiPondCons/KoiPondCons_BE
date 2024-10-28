package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.enums.Role;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderUpdateRequest;
import com.koiteampro.koipondcons.models.response.AccountResponse;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.MaintenanceOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceOrderService {

    @Autowired
    private MaintenanceOrderRepository maintenanceOrderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public MaintenanceOrderResponse createMaintenanceOrder(MaintenanceOrderRequest maintenanceOrderRequest){
        MaintenanceOrder maintenanceOrder = modelMapper.map(maintenanceOrderRequest, MaintenanceOrder.class);

        Customer customer = customerService.getCurrentCustomer();
        maintenanceOrder.setCustomer(customer);

        maintenanceOrder.setStatus(MaintenanceOrderStatus.REQUESTED);

        maintenanceOrder.setRequestDate(LocalDateTime.now());
        maintenanceOrderRepository.save(maintenanceOrder);

        return setToMaintenanceOrderResponse(maintenanceOrder);
    }

    public MaintenanceOrderResponse setToMaintenanceOrderResponse(MaintenanceOrder maintenanceOrder){
        MaintenanceOrderResponse maintenanceOrderResponse = new MaintenanceOrderResponse();
        maintenanceOrderResponse.setId(maintenanceOrder.getId());
        maintenanceOrderResponse.setConstructionOrderId(maintenanceOrder.getConstructionOrderId());
        maintenanceOrderResponse.setCustomerName(maintenanceOrder.getCustomerName());
        maintenanceOrderResponse.setCustomerEmail(maintenanceOrder.getCustomer().getAccount().getEmail());
        maintenanceOrderResponse.setCustomerPhone(maintenanceOrder.getCustomerPhone());
        maintenanceOrderResponse.setPondAddress(maintenanceOrder.getPondAddress());
        maintenanceOrderResponse.setPondVolume(maintenanceOrder.getPondVolume());
        maintenanceOrderResponse.setCustomerDescription(maintenanceOrder.getCustomerDescription());
        maintenanceOrderResponse.setRequestDate(maintenanceOrder.getRequestDate());
        maintenanceOrderResponse.setEndDate(maintenanceOrder.getEndDate());
        maintenanceOrderResponse.setPrice(maintenanceOrder.getPrice());
        maintenanceOrderResponse.setStatus(maintenanceOrder.getStatus());
        maintenanceOrderResponse.setStatusDescription(maintenanceOrder.getStatus().getDescription());
        maintenanceOrderResponse.setWarranted(maintenanceOrder.isWarranted());
        if (maintenanceOrder.getConsultantAccount() != null && maintenanceOrder.getConsultantAccount().getName() != null
                && maintenanceOrder.getConsultantAccount().getPhone() != null) {
            maintenanceOrderResponse.setConsultantName(maintenanceOrder.getConsultantAccount().getName());
            maintenanceOrderResponse.setConsultantPhone(maintenanceOrder.getConsultantAccount().getPhone());
        }
        if (maintenanceOrder.getConstructorAccount() != null && maintenanceOrder.getConstructorAccount().getName() != null) {
            maintenanceOrderResponse.setConstructorName(maintenanceOrder.getConstructorAccount().getName());
        }
        return maintenanceOrderResponse;
    }

    public List<MaintenanceOrderResponse> findMaintenanceOrderByCustomerAndBeforeNow(LocalDateTime now, Customer customer){
        List<MaintenanceOrder> maintenanceOrders =  maintenanceOrderRepository.findMaintenanceOrderByRequestDateBeforeAndCustomer(now, customer);
        List<MaintenanceOrderResponse> maintenanceOrderResponses = new ArrayList<>();
        for (MaintenanceOrder maintenanceOrder : maintenanceOrders){
            MaintenanceOrderResponse maintenanceOrderResponse = setToMaintenanceOrderResponse(maintenanceOrder);
            maintenanceOrderResponses.add(maintenanceOrderResponse);
        }
        return maintenanceOrderResponses;
    }
//
    public MaintenanceOrderResponse updateMaintenanceOrder(long id, MaintenanceOrderUpdateRequest maintenanceOrderUpdateRequest){

        Optional<MaintenanceOrder> maintenanceOrder = maintenanceOrderRepository.findById(id);

        if(maintenanceOrder.isPresent()) {
            MaintenanceOrder maintenanceOrderUpdate = maintenanceOrder.get();
            MaintenanceOrder maintenanceOrderInfoUpdate = modelMapper.map(maintenanceOrderUpdateRequest, MaintenanceOrder.class);

            maintenanceOrderUpdate.setWarranted(maintenanceOrderInfoUpdate.isWarranted());
            maintenanceOrderUpdate.setPondVolume(maintenanceOrderInfoUpdate.getPondVolume());
            maintenanceOrderUpdate.setEndDate(maintenanceOrderInfoUpdate.getEndDate());
            maintenanceOrderUpdate.setStatus(maintenanceOrderInfoUpdate.getStatus());
            maintenanceOrderUpdate.setPrice(maintenanceOrderInfoUpdate.getPrice());

            maintenanceOrderRepository.save(maintenanceOrderUpdate);
            return setToMaintenanceOrderResponse(maintenanceOrderUpdate);
        }else{
            throw new RuntimeException("No maintenance order found with id " + id);
        }
    }

    public List<MaintenanceOrderResponse> findMaintenanceOrderByConsultantAndBeforeNow(LocalDateTime now, Account consultant){
        List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.findMaintenanceOrderByRequestDateBeforeAndConsultantAccount(now, consultant);
        List<MaintenanceOrderResponse> maintenanceOrderResponses = new ArrayList<>();
        for (MaintenanceOrder maintenanceOrder : maintenanceOrders){
            MaintenanceOrderResponse maintenanceOrderResponse = setToMaintenanceOrderResponse(maintenanceOrder);
            maintenanceOrderResponses.add(maintenanceOrderResponse);
        }
        return maintenanceOrderResponses;
    }

    public MaintenanceOrderResponse getActiveMaintenanceOrderOfConstructor() {
        Account constructorAccount = accountService.getCurrentAccount();
        MaintenanceOrder maintenanceOrder = maintenanceOrderRepository.findByConstructorAccountAndStatus(constructorAccount, MaintenanceOrderStatus.PROCESSING);
        if (maintenanceOrder != null)
            return setToMaintenanceOrderResponse(maintenanceOrder);
        else
            throw new NotFoundException("Maintenance order not found!");
    }

    public List<MaintenanceOrderResponse> getFinishedMaintenanceOrderOfConstructor(){
        Account constructorAccount = accountService.getCurrentAccount();
        List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.getFinishedMaintenanceOrderOfConstructor(constructorAccount);
        List<MaintenanceOrderResponse> maintenanceOrderResponses = new ArrayList<>();
        for (MaintenanceOrder maintenanceOrder : maintenanceOrders){
            MaintenanceOrderResponse maintenanceOrderResponse = setToMaintenanceOrderResponse(maintenanceOrder);
            maintenanceOrderResponses.add(maintenanceOrderResponse);
        }
        return maintenanceOrderResponses;
    }

    public List<MaintenanceOrderResponse> getAllConfirmedMaintenanceOrders() {
        List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.getAllConfirmedMaintenanceOrders();
        List<MaintenanceOrderResponse> maintenanceOrderResponses = new ArrayList<>();
        for (MaintenanceOrder maintenanceOrder : maintenanceOrders){
            MaintenanceOrderResponse maintenanceOrderResponse = setToMaintenanceOrderResponse(maintenanceOrder);
            maintenanceOrderResponses.add(maintenanceOrderResponse);
        }
        return maintenanceOrderResponses;
    }

    public List<MaintenanceOrderResponse> getAllRequestedMaintenanceOrders() {
        List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.getAllRequestedMaintenanceOrders();
        List<MaintenanceOrderResponse> maintenanceOrderResponses = new ArrayList<>();
        for (MaintenanceOrder maintenanceOrder : maintenanceOrders){
            MaintenanceOrderResponse maintenanceOrderResponse = setToMaintenanceOrderResponse(maintenanceOrder);
            maintenanceOrderResponses.add(maintenanceOrderResponse);
        }
        return maintenanceOrderResponses;
    }

    public void setConsultantToOrder(long orderId) {
        Optional<MaintenanceOrder> maintenanceOrder = maintenanceOrderRepository.findById(orderId);
        Account consultingAccount = accountService.getCurrentAccount();

        if (maintenanceOrder.isPresent()) {
            MaintenanceOrder maintenanceOrderUpdate = maintenanceOrder.get();
            maintenanceOrderUpdate.setConsultantAccount(consultingAccount);
            maintenanceOrderRepository.save(maintenanceOrderUpdate);
        }
        else
            throw new NotFoundException("No maintenance order found with id " + orderId);
    }

    public void setConstructorToOrder(long orderId, long constructorId) {
        Optional<MaintenanceOrder> maintenanceOrder = maintenanceOrderRepository.findById(orderId);
        if (maintenanceOrder.isPresent()) {
            MaintenanceOrder maintenanceOrderUpdate = maintenanceOrder.get();
            Optional<Account> constructorAccount = accountRepository.findById(constructorId);
            if (constructorAccount.isPresent()) {
                Account constructor = constructorAccount.get();
                maintenanceOrderUpdate.setConstructorAccount(constructor);
                maintenanceOrderRepository.save(maintenanceOrderUpdate);
            }
            else
                throw new NotFoundException("No constructor found with id " + constructorId);
        }
        else
            throw new NotFoundException("No maintenance order found with id " + orderId);
    }

    public List<AccountResponse> getConstructorsNotInMaintaining() {
        List<Account> accounts = new ArrayList<>();
        List<Long> accountIds = new ArrayList<>();

        try {
            accountIds = maintenanceOrderRepository.findStaffIdsWithUnfinishedWorks();
        } catch (Exception e) {
            accountIds = null;
        }

        try {
            if (accountIds == null || accountIds.isEmpty()) {
                accounts = accountRepository.findAccountByRoleAndIsEnabledTrue(Role.CONSTRUCTOR);
            } else {
                accounts = accountRepository.findByIdNotInAndRoleLike(accountIds, Role.CONSTRUCTOR);
            }
        } catch (Exception e) {
            throw new NotFoundException("Staff not found!");
        }

        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : accounts) {
            AccountResponse accountResponse = accountService.getAccountResponse(account);
            accountResponses.add(accountResponse);
        }

        return accountResponses;
    }

    public MaintenanceOrderResponse getByMaintenanceOrderId(long orderId) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderRepository.findMaintenanceOrderById(orderId);
        return modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class);

    }
}

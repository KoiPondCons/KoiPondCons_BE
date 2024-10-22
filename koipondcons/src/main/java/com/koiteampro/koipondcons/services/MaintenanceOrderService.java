package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.repositories.MaintenanceOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceOrderService {

    @Autowired
    private MaintenanceOrderRepository maintenanceOrderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    public MaintenanceOrderResponse createMaintenanceOrder(MaintenanceOrderRequest maintenanceOrderRequest){
        MaintenanceOrder maintenanceOrder = modelMapper.map(maintenanceOrderRequest, MaintenanceOrder.class);

        Customer customer = customerService.getCurrentCustomer();
        maintenanceOrder.setCustomer(customer);

        maintenanceOrder.setCreateAt(LocalDate.now());
        maintenanceOrderRepository.save(maintenanceOrder);

        return modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class);
    }

    public MaintenanceOrderResponse getActiveMaintenanceOrderOfConstructor() {
        Account constructorAccount = authenticationService.getCurrentAccount();
        MaintenanceOrder maintenanceOrder = maintenanceOrderRepository.findByConstructorAccountIdAndStatusLike(constructorAccount.getId(), MaintenanceOrderStatus.PROCESSING + "");
        if (maintenanceOrder != null)
            return modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class);
        else
            throw new NotFoundException("Maintenance order not found!");
    }

//    public List<MaintenanceOrder> getAllConfirmedMaintenanceOrders() {
//        List<MaintenanceOrder> = maintenanceOrderRepository.
//
//    }
}

package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.repositories.MaintenanceOrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceOrderService {

    @Autowired
    private MaintenanceOrderRepository maintenanceOrderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    public MaintenanceOrder createMaintenanceOrder(MaintenanceOrderRequest maintenanceOrderRequest){
        MaintenanceOrder maintenanceOrder = modelMapper.map(maintenanceOrderRequest, MaintenanceOrder.class);
//
//        maintenanceOrder.setWarranted(maintenanceOrder.isWarranted());
//
//        maintenanceOrder.setConstructionOrder(maintenanceOrderRequest.getConstructionOrder());

        Customer customer = customerService.getCurrentCustomer();
        maintenanceOrder.setCustomer(customer);
//        maintenanceOrder.setCustomerName(maintenanceOrderRequest.getCustomerName());
//        maintenanceOrder.setCustomerPhone(maintenanceOrderRequest.getCustomerPhone());

//        maintenanceOrder.setPondAddress(maintenanceOrderRequest.getPondAddress());
//        maintenanceOrder.setPondVolume(maintenanceOrderRequest.getPondVolume());

        maintenanceOrderRepository.save(maintenanceOrder);
        return  maintenanceOrder;
    }
}

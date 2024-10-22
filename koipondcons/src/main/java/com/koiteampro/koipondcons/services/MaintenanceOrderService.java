package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.MaintenanceOrder;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderRequest;
import com.koiteampro.koipondcons.models.request.MaintenanceOrderUpdateRequest;
import com.koiteampro.koipondcons.models.response.MaintenanceOrderResponse;
import com.koiteampro.koipondcons.repositories.MaintenanceOrderRepository;
import com.sun.tools.javac.Main;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
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

    public MaintenanceOrderResponse createMaintenanceOrder(MaintenanceOrderRequest maintenanceOrderRequest){
        MaintenanceOrder maintenanceOrder = modelMapper.map(maintenanceOrderRequest, MaintenanceOrder.class);

        Customer customer = customerService.getCurrentCustomer();
        maintenanceOrder.setCustomer(customer);

        maintenanceOrder.setStatus(MaintenanceOrderStatus.REQUESTED);

        maintenanceOrder.setCreateAt(LocalDate.now());
        maintenanceOrderRepository.save(maintenanceOrder);

        return modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class);
    }

    public List<MaintenanceOrderResponse> findMaintenanceOrderByCustomerAndBeforeNow(LocalDate now, Customer customer){
        List<MaintenanceOrder> maintenanceOrders =  maintenanceOrderRepository.findMaintenanceOrderByCreateAtBeforeAndCustomer(now, customer);
        return maintenanceOrders.stream().map(maintenanceOrder -> modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class)).collect(Collectors.toList());
    }

    public MaintenanceOrderResponse updateMaintenanceOrder(long id, MaintenanceOrderUpdateRequest maintenanceOrderUpdateRequest){

        Optional<MaintenanceOrder> maintenanceOrder = maintenanceOrderRepository.findById(id);

        if(maintenanceOrder.isPresent()) {
            MaintenanceOrder maintenanceOrderUpdate = maintenanceOrder.get();
            MaintenanceOrder maintenanceOrderInfoUpdate = modelMapper.map(maintenanceOrderUpdateRequest, MaintenanceOrder.class);

            maintenanceOrderUpdate.setConsultantAccount(maintenanceOrderInfoUpdate.getConsultantAccount());
            maintenanceOrderUpdate.setConstructorAccount(maintenanceOrderInfoUpdate.getConstructorAccount());

            maintenanceOrderUpdate.setWarranted(maintenanceOrderInfoUpdate.isWarranted());
            maintenanceOrderUpdate.setPondVolume(maintenanceOrderInfoUpdate.getPondVolume());
            maintenanceOrderUpdate.setEndDate(maintenanceOrderInfoUpdate.getEndDate());
            maintenanceOrderUpdate.setStatus(maintenanceOrderInfoUpdate.getStatus());

            maintenanceOrderRepository.save(maintenanceOrderUpdate);
            return modelMapper.map(maintenanceOrderUpdate, MaintenanceOrderResponse.class);
        }else{
            throw new RuntimeException("No maintenance order found with id " + id);
        }
    }

    public List<MaintenanceOrderResponse> findMaintenanceOrderByConsultantAndBeforeNow(LocalDate now, Account consultant){
        List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.findMaintenanceOrderByCreateAtBeforeAndConsultantAccount(now, consultant);
        return maintenanceOrders.stream().map(maintenanceOrder -> modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class)).collect(Collectors.toList());
    }

    public List<MaintenanceOrderResponse> findMaintenanceOrderBeforeNowAndProcessedOrFinishedAndConstructor(LocalDate now,Account constructor, MaintenanceOrderStatus status){
        if(status.equals(MaintenanceOrderStatus.PROCESSED) || status.equals(MaintenanceOrderStatus.FINISHED)) {
            List<MaintenanceOrder> maintenanceOrders = maintenanceOrderRepository.findMaintenanceOrderByCreateAtBeforeAndStatusAndConstructorAccount(now,status, constructor);
            return maintenanceOrders.stream().map(maintenanceOrder -> modelMapper.map(maintenanceOrder, MaintenanceOrderResponse.class)).collect(Collectors.toList());

        }
        return null;
    }
}

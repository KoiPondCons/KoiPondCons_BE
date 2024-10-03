package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.Quotation;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConstructionOrderService {

    @Autowired
    private ConstructionOrderRepository constructionOrderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    ModelMapper modelMapper;

    public ConstructionOrderResponse createConstructionOrder(ConstructionOrderRequest constructionOrderRequest) {

        ConstructionOrder constructionOrder = modelMapper.map(constructionOrderRequest, ConstructionOrder.class);

        Quotation quotation = new Quotation();

        quotation.setPondVolume(constructionOrderRequest.getPondVolume());

        quotation.setConstructionOrder(constructionOrder);

        constructionOrder.setQuotation(quotation);

        Account account = authenticationService.getCurrentAccount();
        Customer customer = customerRepository.findByAccountId(account.getId());

        constructionOrder.setCustomer(customer);
        customer.getConstructionOrderList().add(constructionOrder);

        System.out.println(customer.getId());


        constructionOrderRepository.save(constructionOrder);

        return modelMapper.map(constructionOrder, ConstructionOrderResponse.class);
    }

    public ConstructionOrderResponse updateConstructionOrder(long id, ConstructionOrderUpdateRequest constructionOrderUpdateRequest) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(id);

        if (constructionOrder.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();
            ConstructionOrder constructionOrderInfoUpdate = modelMapper.map(constructionOrderUpdateRequest, ConstructionOrder.class);


            constructionOrderUpdate.setStatus(constructionOrderInfoUpdate.getStatus());
            constructionOrderUpdate.setCustomerName(constructionOrderInfoUpdate.getCustomerName());
            constructionOrderUpdate.setCustomerEmail(constructionOrderInfoUpdate.getCustomerEmail());
            constructionOrderUpdate.setCustomerPhone(constructionOrderInfoUpdate.getCustomerPhone());
            constructionOrderUpdate.setPondAddress(constructionOrderInfoUpdate.getPondAddress());


            constructionOrderRepository.save(constructionOrderUpdate);

            return modelMapper.map(constructionOrderUpdate, ConstructionOrderResponse.class);
        } else {
            throw new RuntimeException("No construction order found with id " + id);
        }
    }

    public void setConsultingToOrder(long constructionOrderId, long consultantId) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(constructionOrderId);
        Optional<Account> consultingAccount = accountRepository.findById(consultantId);


        if (constructionOrder.isPresent() && consultingAccount.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();
            constructionOrderUpdate.setConsultantAccount(consultingAccount.get());

            constructionOrderRepository.save(constructionOrderUpdate);
        } else {
            throw new NotFoundException("Construction or consulting not found");
        }
    }

    public List<ConstructionOrderResponse> getAllConstructionOrdersByStatus(ConstructionOrderStatus status) {
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByStatusIs(status);
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(modelMapper.map(constructionOrder, ConstructionOrderResponse.class));
        }
        return constructionOrderResponses;
    }

    public List<ConstructionOrderResponse> getAllConstructionOrders() {
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAll();
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(modelMapper.map(constructionOrder, ConstructionOrderResponse.class));
        }

        return constructionOrderResponses;
    }
}

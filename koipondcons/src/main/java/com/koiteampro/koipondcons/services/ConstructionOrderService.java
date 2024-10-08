package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.*;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
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
    private StaffConstructionDetailRepository staffConstructionDetailRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private ComboConstructionItemService comboConstructionItemService;
    @Autowired
    ModelMapper modelMapper;

    public ConstructionOrderResponse createConstructionOrder(ConstructionOrderRequest constructionOrderRequest) {

        ConstructionOrder constructionOrder = modelMapper.map(constructionOrderRequest, ConstructionOrder.class);

        ////////////////////////////////////////////

        Quotation quotation = new Quotation();

        quotation.setPondVolume(constructionOrderRequest.getPondVolume());

        quotation.setConstructionOrder(constructionOrder);

        constructionOrder.setQuotation(quotation);

        //////////////////////////////////////////

        DesignDrawing drawing = new DesignDrawing();

        drawing.setConstructionOrder(constructionOrder);

        constructionOrder.setDesignDrawing(drawing);

        //////////////////////////////////////////

        Customer customer = customerService.getCurrentCustomer();

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
            ConstructionOrderResponse constructionOrderResponse = modelMapper.map(constructionOrder, ConstructionOrderResponse.class);
            constructionOrderResponse.setStatusDescription(constructionOrder.getStatus().getDescription());
            constructionOrderResponses.add(constructionOrderResponse);
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

    public List<ConstructionOrderResponse> getAllConstructionOrdersOfCustomer() {
        Customer customer = customerService.getCurrentCustomer();

        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByCustomerId(customer.getId());
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(modelMapper.map(constructionOrder, ConstructionOrderResponse.class));
        }

        return constructionOrderResponses;
    }

    public void setConstructorToOrder(long constructionOrderId, long constructorId) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(constructionOrderId);
        Optional<Account> constructorAccount = accountRepository.findById(constructorId);
        if (constructionOrder.isPresent() && constructorAccount.isPresent()) {
            Quotation quotation = constructionOrder.get().getQuotation();
            List<ComboConstructionItem> comboConstructionItems = quotation.getCombo().getComboConstructionItemList();


            for(ComboConstructionItem comboConstructionItem : comboConstructionItems) {
                StaffConstructionDetail staffConstructionDetail = new StaffConstructionDetail();
                staffConstructionDetail.setConstructionItem(comboConstructionItem);
                staffConstructionDetail.setConstructionOrder(constructionOrder.get());
                staffConstructionDetail.setConstructorAccount(constructorAccount.get());
                staffConstructionDetailRepository.save(staffConstructionDetail);
            }
        }
    }

}

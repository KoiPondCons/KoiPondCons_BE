package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.*;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequestStatusUpdate;
import com.koiteampro.koipondcons.models.response.ConstructionOrderRequestedStatusResponse;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponse;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
import com.koiteampro.koipondcons.models.response.ConstructionOrderResponseCustomerHistory;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.CustomerRepository;
import com.koiteampro.koipondcons.repositories.StaffConstructionDetailRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
    private AccountService accountService;

    @Autowired
    StaffConstructionDetailService staffConstructionDetailService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DesignDrawingService designDrawingService;

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

    public String updateConstructionOrderStatus(long id, ConstructionOrderRequestStatusUpdate order) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(id);

        if (constructionOrder.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();

            constructionOrderUpdate.setStatus(order.getStatus());
            if (constructionOrderUpdate.getStatus() == ConstructionOrderStatus.CLOSED) {
                constructionOrderUpdate.getCustomer().setTotal_points(constructionOrderUpdate.getCustomer().getTotal_points() + constructionOrderUpdate.getQuotation().getFinalPrice().divide(new BigDecimal(1000000), 0, RoundingMode.FLOOR).intValueExact());
                constructionOrderUpdate.setWarrantyEndDate(LocalDate.now().plusYears(1));
                constructionOrderUpdate.setWarrantyRemaining(2);
            }

            constructionOrderRepository.save(constructionOrderUpdate);

            return "Update successfully";
        } else {
            throw new RuntimeException("No construction order found with id " + id);
        }
    }

    public ConstructionOrderResponse updateConstructionOrder(long id, ConstructionOrderUpdateRequest constructionOrderUpdateRequest) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(id);

        if (constructionOrder.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();
            ConstructionOrder constructionOrderInfoUpdate = modelMapper.map(constructionOrderUpdateRequest, ConstructionOrder.class);

            constructionOrderUpdate.setStatus(constructionOrderInfoUpdate.getStatus());
            if (constructionOrderUpdate.getStatus() == ConstructionOrderStatus.CLOSED) {
                constructionOrderUpdate.getCustomer().setTotal_points(constructionOrderUpdate.getCustomer().getTotal_points() + constructionOrderUpdate.getQuotation().getFinalPrice().divide(new BigDecimal(1000000), 0, RoundingMode.FLOOR).intValueExact());
                constructionOrderUpdate.setWarrantyEndDate(LocalDate.now().plusYears(1));
                constructionOrderUpdate.setWarrantyRemaining(2);
            }
            constructionOrderUpdate.setCustomerName(constructionOrderInfoUpdate.getCustomerName());
            constructionOrderUpdate.setCustomerEmail(constructionOrderInfoUpdate.getCustomerEmail());
            constructionOrderUpdate.setCustomerPhone(constructionOrderInfoUpdate.getCustomerPhone());
            constructionOrderUpdate.setPondAddress(constructionOrderInfoUpdate.getPondAddress());
            constructionOrderUpdate.setDesigned(constructionOrderInfoUpdate.isDesigned());
            constructionOrderUpdate.setConfirmedDate(constructionOrderInfoUpdate.getConfirmedDate());

            constructionOrderRepository.save(constructionOrderUpdate);

            return modelMapper.map(constructionOrderUpdate, ConstructionOrderResponse.class);
        } else {
            throw new RuntimeException("No construction order found with id " + id);
        }
    }

    public void setConsultingToOrder(long constructionOrderId) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(constructionOrderId);
        Account consultingAccount = accountService.getCurrentAccount();


        if (constructionOrder.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();
            constructionOrderUpdate.setConsultantAccount(consultingAccount);

            constructionOrderRepository.save(constructionOrderUpdate);
        } else {
            throw new NotFoundException("Construction or consulting not found");
        }
    }

    public ConstructionOrderResponse getCurrentOrderOfConstructor() {
        Account consultantAccount = accountService.getCurrentAccount();
        List<StaffConstructionDetail> staffConstructionDetails = staffConstructionDetailRepository.findByIsFinishedFalseAndConstructorAccountId(consultantAccount.getId());

        if (staffConstructionDetails != null && !staffConstructionDetails.isEmpty()) {
            return setInfoForConstructionOrder(staffConstructionDetails.getFirst().getConstructionOrder());
        } else {
            throw new NotFoundException("No construction order found");
        }
    }

    public List<ConstructionOrderResponse> getAllConstructionOrdersByStatus(ConstructionOrderStatus status) {
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByStatusIs(status);
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConstructionOrder(constructionOrder));
        }
        return constructionOrderResponses;
    }

    public List<ConstructionOrderRequestedStatusResponse> getAllConstructionOrdersByRequestedStatus(ConstructionOrderStatus status) {
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByStatusIs(status);
        List<ConstructionOrderRequestedStatusResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForOrderRequestedStatus(constructionOrder));
        }
        return constructionOrderResponses;
    }

    public List<ConstructionOrderResponse> getAllConstructionOrdersOfConsultantByStatus(ConstructionOrderStatus status) {
        Account account = accountService.getCurrentAccount();

        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByConsultantAccountIdAndStatusIs(account.getId(), status);
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConstructionOrder(constructionOrder));
        }

        return constructionOrderResponses;
    }

    public List<ConstructionOrderResponse> getAllConstructionOrders() {
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAll();
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConstructionOrder(constructionOrder));
        }

        return constructionOrderResponses;
    }

    public List<ConstructionOrderResponseCustomerHistory> getAllConstructionOrdersOfCustomer() {
        Customer customer = customerService.getCurrentCustomer();

        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByCustomerIdAndStatusNot(customer.getId(), ConstructionOrderStatus.CANCELED);
        List<ConstructionOrderResponseCustomerHistory> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForCustomerResponseHistory(constructionOrder));
        }

        return constructionOrderResponses;
    }

    public List<ConstructionOrderResponse> getAllConstructionOrdersOfConsultant() {
        Account account = accountService.getCurrentAccount();

        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByConsultantAccountIdAndStatusNot(account.getId(), ConstructionOrderStatus.CANCELED);
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConstructionOrder(constructionOrder));
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

    public ConstructionOrderResponse getOrderById(long constructionOrderId) {
        Optional<ConstructionOrder> constructionOrder = constructionOrderRepository.findById(constructionOrderId);

        if (constructionOrder.isPresent()) {
            ConstructionOrder constructionOrderUpdate = constructionOrder.get();
            return setInfoForConstructionOrder(constructionOrderUpdate);
        } else {
            throw new NotFoundException("Construction order not found with id " + constructionOrderId);
        }
    }

    public ConstructionOrderResponse setInfoForConstructionOrder(ConstructionOrder constructionOrder) {
        ConstructionOrderResponse constructionOrderResponse = modelMapper.map(constructionOrder, ConstructionOrderResponse.class);
        constructionOrderResponse.setDesignDrawingResponse(designDrawingService.getDesignDrawingResponse(constructionOrder.getDesignDrawing()));
        constructionOrderResponse.setQuotationResponse(quotationService.getQuotationResponse(constructionOrder.getQuotation()));
        constructionOrderResponse.setStatusDescription(constructionOrder.getStatus().getDescription());
        constructionOrderResponse.setConstructorAccount(staffConstructionDetailService.getConstructorOfConstructionOrder(constructionOrderResponse.getId()));
        constructionOrderResponse.setConsOrderPaymentList(constructionOrder.getConsOrderPaymentList());
        constructionOrderResponse.setConstructionProgress(staffConstructionDetailService.getProgressByConstructionOrder(constructionOrder.getId()));
        constructionOrderResponse.setWarrantyEndDate(constructionOrder.getWarrantyEndDate());
        constructionOrderResponse.setWarrantyRemaining(constructionOrder.getWarrantyRemaining());
        return constructionOrderResponse;
    }

    public ConstructionOrderResponseCustomerHistory setInfoForCustomerResponseHistory(ConstructionOrder constructionOrder) {
        ConstructionOrderResponseCustomerHistory constructionOrderResponseCustomerHistory = modelMapper.map(constructionOrder, ConstructionOrderResponseCustomerHistory.class);
        constructionOrderResponseCustomerHistory.setStatusDescription(constructionOrder.getStatus().getDescription());
//        constructionOrderResponseCustomerHistory.setDesignDrawingResponse(designDrawingService.getDesignDrawingResponse(constructionOrder.getDesignDrawing()));
//        constructionOrderResponseCustomerHistory.setQuotationResponse(quotationService.getQuotationResponse(constructionOrder.getQuotation()));
        return constructionOrderResponseCustomerHistory;
    }

    public ConstructionOrderRequestedStatusResponse setInfoForOrderRequestedStatus(ConstructionOrder constructionOrder) {
        ConstructionOrderRequestedStatusResponse constructionOrderRequestedStatusResponse = modelMapper.map(constructionOrder, ConstructionOrderRequestedStatusResponse.class);
        constructionOrderRequestedStatusResponse.setStatusDescription(constructionOrder.getStatus().getDescription());

        return constructionOrderRequestedStatusResponse;
    }

    public List<ConstructionOrder> getFinishedOrdersByConstructorID(long constructorId){
        return constructionOrderRepository.findFinishedOrdersByConstructorID(constructorId);
    }

    public List<ConstructionOrderResponse> getFinishedOrdersByCurrentConstructor(){
        Account constructorAccount = accountService.getCurrentAccount();
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findFinishedOrdersByConstructorID(constructorAccount.getId());
        List<ConstructionOrderResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConstructionOrder(constructionOrder));
        }
        return constructionOrderResponses;
    }


}

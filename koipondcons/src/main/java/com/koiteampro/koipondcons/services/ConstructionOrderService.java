package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.*;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequest;
import com.koiteampro.koipondcons.models.request.ConstructionOrderRequestStatusUpdate;
import com.koiteampro.koipondcons.models.response.*;
import com.koiteampro.koipondcons.models.request.ConstructionOrderUpdateRequest;
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

    @Autowired
    private EmailService emailService;

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

            if (constructionOrderUpdate.getStatus() == ConstructionOrderStatus.CONSTRUCTED) {
                EmailPaymentDetail emailPaymentDetail = new EmailPaymentDetail();
                emailPaymentDetail.setReceiver(constructionOrderUpdate.getCustomer().getAccount());
                emailPaymentDetail.setSubject("[KoiPondCons] Thông Báo Thanh Toán Đợt 3 – Bàn Giao Hồ Cá Koi");
                emailPaymentDetail.setText1("Chúng tôi xin gửi lời cảm ơn chân thành tới Quý khách vì đã đồng hành cùng chúng tôi trong suốt quá trình thiết kế và thi công hồ cá Koi. Hiện nay, dự án đã hoàn thiện và sẵn sàng cho giai đoạn bàn giao.");
                emailPaymentDetail.setText2("Để chính thức hoàn tất và bàn giao hồ cá Koi theo đúng cam kết, chúng tôi kính đề nghị Quý khách thanh toán đợt 3, cũng là đợt thanh toán cuối cùng theo thỏa thuận. Việc thanh toán này sẽ giúp chúng tôi hoàn tất các thủ tục bàn giao và cung cấp các tài liệu liên quan đến việc bảo trì và chăm sóc hồ cá trong thời gian tới.");
                emailPaymentDetail.setText3("Quý khách có thể thanh toán qua chuyển khoản ngân hàng hoặc các phương thức thanh toán đã được cung cấp. Sau khi nhận được thanh toán, chúng tôi sẽ sắp xếp lịch bàn giao chi tiết và hướng dẫn Quý khách các bước chăm sóc hồ cá để đảm bảo môi trường sống tốt nhất cho Koi.");
                emailPaymentDetail.setText4("Xin chân thành cảm ơn Quý khách đã đồng hành cùng chúng tôi trong từng giai đoạn của dự án. Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua số điện thoại hoặc email dưới đây để được hỗ trợ.");
                emailPaymentDetail.setText5("Trân trọng, KoiPondCons");
                emailService.sendFirstPaymentEmail(emailPaymentDetail);
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

    public List<ConstructionOrderResponseCustomerHistoryForManager> getAllConstructionOrdersOfCustomerById(long id) {
        long customerId = customerService.getCustomerByAccountId(id).getId();
        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByCustomerIdAndStatusNot(customerId, ConstructionOrderStatus.CANCELED);
        List<ConstructionOrderResponseCustomerHistoryForManager> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForCustomerResponseHistoryForManager(constructionOrder));
        }

        return constructionOrderResponses;
    }

    public List<ConstructionOrderConsultantResponse> getAllConstructionOrdersOfConsultant() {
        Account account = accountService.getCurrentAccount();

        List<ConstructionOrder> constructionOrders = constructionOrderRepository.findAllByConsultantAccountIdAndStatusNot(account.getId(), ConstructionOrderStatus.CANCELED);
        List<ConstructionOrderConsultantResponse> constructionOrderResponses = new ArrayList<>();

        for (ConstructionOrder constructionOrder : constructionOrders) {
            constructionOrderResponses.add(setInfoForConsultantOrderResponse(constructionOrder));
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

    public ConstructionOrderResponseCustomerHistoryForManager setInfoForCustomerResponseHistoryForManager(ConstructionOrder constructionOrder) {
        ConstructionOrderResponseCustomerHistoryForManager constructionOrderResponseCustomerHistoryForManager = modelMapper.map(constructionOrder, ConstructionOrderResponseCustomerHistoryForManager.class);
        constructionOrderResponseCustomerHistoryForManager.setStatusDescription(constructionOrder.getStatus().getDescription());
//        constructionOrderResponseCustomerHistory.setDesignDrawingResponse(designDrawingService.getDesignDrawingResponse(constructionOrder.getDesignDrawing()));
//        constructionOrderResponseCustomerHistory.setQuotationResponse(quotationService.getQuotationResponse(constructionOrder.getQuotation()));
        return constructionOrderResponseCustomerHistoryForManager;
    }

    public ConstructionOrderRequestedStatusResponse setInfoForOrderRequestedStatus(ConstructionOrder constructionOrder) {
        ConstructionOrderRequestedStatusResponse constructionOrderRequestedStatusResponse = modelMapper.map(constructionOrder, ConstructionOrderRequestedStatusResponse.class);
        constructionOrderRequestedStatusResponse.setStatusDescription(constructionOrder.getStatus().getDescription());
        constructionOrderRequestedStatusResponse.setCustomerAccountId(constructionOrder.getCustomer().getAccount().getId());
        return constructionOrderRequestedStatusResponse;
    }

    public ConstructionOrderConsultantResponse setInfoForConsultantOrderResponse(ConstructionOrder constructionOrder) {
        ConstructionOrderConsultantResponse constructionOrderConsultantResponse = modelMapper.map(constructionOrder, ConstructionOrderConsultantResponse.class);
        constructionOrderConsultantResponse.setStatusDescription(constructionOrder.getStatus().getDescription());
        constructionOrderConsultantResponse.getQuotation().setStatusDescription(constructionOrderConsultantResponse.getQuotation().getStatus().getDescription());
        constructionOrderConsultantResponse.getDesignDrawing().setStatusDescription(constructionOrderConsultantResponse.getDesignDrawing().getStatus().getDescription());
        constructionOrderConsultantResponse.setCustomerAccountId(constructionOrder.getCustomer().getAccount().getId());
        return constructionOrderConsultantResponse;
    }

//    public ConstructionOrderManagerResponse setInfoForManagerOrderResponse(ConstructionOrder constructionOrder) {
//        ConstructionOrderManagerResponse constructionOrderManagerResponse = modelMapper.map(constructionOrder, ConstructionOrderManagerResponse.class);
//        constructionOrderManagerResponse.setStatusDescription(constructionOrder.getStatus().getDescription());
//        constructionOrderManagerResponse.getQuotation().setStatusDescription(constructionOrderManagerResponse.getQuotation().getStatus().getDescription());
//        constructionOrderManagerResponse.getDesignDrawing().setStatusDescription(constructionOrderManagerResponse.getDesignDrawing().getStatus().getDescription());
//        constructionOrderManagerResponse.setConstructorAccount(staffConstructionDetailService.getConstructorOfConstructionOrder(constructionOrderManagerResponse.getId()));
//        return constructionOrderManagerResponse;
//    }

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

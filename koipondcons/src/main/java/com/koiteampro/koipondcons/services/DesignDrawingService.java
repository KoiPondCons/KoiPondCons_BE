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
import com.koiteampro.koipondcons.models.response.EmailPaymentDetail;
import com.koiteampro.koipondcons.models.response.OrderCustomerResponse;
import com.koiteampro.koipondcons.repositories.AccountRepository;
import com.koiteampro.koipondcons.repositories.ConstructionOrderRepository;
import com.koiteampro.koipondcons.repositories.DesignDrawingRepository;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForReadableInstant;
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
    private AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConsOrderPaymentService consOrderPaymentService;

    @Autowired
    private EmailService emailService;


    public void updateDesignDrawing(long id, DesignDrawingRequest designDrawingRequest) {
        Optional<DesignDrawing> designDrawingOptional = designDrawingRepository.findById(id);

        if (designDrawingOptional.isPresent()) {
            DesignDrawing designDrawing = designDrawingOptional.get();
            designDrawing.setDesignerAccount(designDrawingRequest.getDesignerAccount());
            designDrawing.setDesignFile(designDrawingRequest.getDesignFile());
            designDrawing.setStatus(designDrawingRequest.getStatus());

            if (designDrawing.getStatus() == DesignDrawingStatus.CUSTOMER_PENDING) {
                EmailPaymentDetail emailPaymentDetail = new EmailPaymentDetail();
                emailPaymentDetail.setReceiver(designDrawing.getConstructionOrder().getCustomer().getAccount());
                emailPaymentDetail.setSubject("[KoiPondCons] Thông Báo Thanh Toán Đợt 2 – Chuẩn Bị Vật Liệu & Thi Công Hồ Cá Koi");
                emailPaymentDetail.setText1("Chúng tôi xin chân thành cảm ơn Quý khách đã hợp tác trong quá trình thiết kế hồ cá Koi. Để tiếp tục dự án đúng tiến độ và bắt đầu giai đoạn thi công, chúng tôi xin phép nhắc Quý khách về đợt thanh toán thứ hai.");
                emailPaymentDetail.setText2("Theo kế hoạch, đợt thanh toán 2 sẽ giúp chúng tôi chuẩn bị đầy đủ vật liệu và nhân lực cần thiết để bắt đầu thi công hồ cá. Do đó, chúng tôi mong Quý khách hoàn tất thanh toán đợt này trong thời gian sớm nhất để đội ngũ thi công có thể nhanh chóng triển khai công việc.");
                emailPaymentDetail.setText3("Thông tin chi tiết về khoản thanh toán đã được gửi trong hợp đồng và bảng báo giá. Quý khách có thể thực hiện thanh toán qua chuyển khoản ngân hàng hoặc các phương thức thanh toán đã được cung cấp.");
                emailPaymentDetail.setText4("Xin chân thành cảm ơn Quý khách đã đồng hành cùng chúng tôi trong từng giai đoạn của dự án. Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua số điện thoại hoặc email dưới đây để được hỗ trợ.");
                emailPaymentDetail.setText5("Trân trọng, KoiPondCons");
                emailService.sendFirstPaymentEmail(emailPaymentDetail);
            }

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
        Account currentAccount = accountService.getCurrentAccount();
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
            accountIds = designDrawingRepository.findStaffIdsWithUnfinishedWorks(DesignDrawingStatus.CUSTOMER_CONFIRMED);
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
            AccountResponse accountResponse = accountService.getAccountResponse(account);
            accountResponses.add(accountResponse);
        }

        return accountResponses;
    }

    public long countFreeDesigners(){
        return getAllFreeDesigners().size();
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

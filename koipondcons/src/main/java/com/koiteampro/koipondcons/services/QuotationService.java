package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboPrice;
import com.koiteampro.koipondcons.entities.Promotion;
import com.koiteampro.koipondcons.entities.Quotation;
import com.koiteampro.koipondcons.enums.QuotationStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.QuotationRequest;
import com.koiteampro.koipondcons.models.response.EmailDetail;
import com.koiteampro.koipondcons.models.response.EmailPaymentDetail;
import com.koiteampro.koipondcons.models.response.QuotationResponse;
import com.koiteampro.koipondcons.repositories.ComboPriceRepository;
import com.koiteampro.koipondcons.repositories.PromotionRepository;
import com.koiteampro.koipondcons.repositories.QuotationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class QuotationService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    QuotationRepository quotationRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    ComboPriceRepository comboPriceRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    private ConstructionOrderService constructionOrderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    ConsOrderPaymentService consOrderPaymentService;

    public void addPromotionToQuotation(@PathVariable long id, @PathVariable long promotionId) {
        Optional<Quotation> quotation = quotationRepository.findById(id);
        Optional<Promotion> promotion = promotionRepository.findById(promotionId);

        if (quotation.isPresent() && promotion.isPresent()) {
            Quotation quotationToAdd = quotation.get();
            Promotion promotionToAdd = promotion.get();

            if (quotationToAdd.getConstructionOrder().getCustomer().getTotal_points() < promotionToAdd.getPointsAvailable()) {
                throw new NotFoundException("Don't enough points to add promotion");
            }

            if (!quotationToAdd.getPromotions().contains(promotionToAdd)) {
                customerService.minusTotalPoint(quotationToAdd.getConstructionOrder().getCustomer().getId(), promotionToAdd.getPointsAvailable());
                quotationToAdd.getPromotions().add(promotionToAdd);
                quotationRepository.save(quotationToAdd);
                updateQuotationPrice(quotationToAdd);
            } else {
                throw new NotFoundException("Promotion already exists");
            }

        } else {
            throw new NotFoundException("Quotation or promotion not found");
        }
    }

    public void removePromotionFromQuotation(@PathVariable long id, @PathVariable long promotionId) {
        Optional<Quotation> quotation = quotationRepository.findById(id);
        Optional<Promotion> promotion = promotionRepository.findById(promotionId);

        if (quotation.isPresent() && promotion.isPresent()) {
            Quotation quotationToAdd = quotation.get();
            Promotion promotionToAdd = promotion.get();

            if (quotationToAdd.getPromotions().contains(promotionToAdd)) {
                customerService.addTotalPoint(quotationToAdd.getConstructionOrder().getCustomer().getId(), promotionToAdd.getPointsAvailable());
                quotationToAdd.getPromotions().remove(promotionToAdd);
                quotationRepository.save(quotationToAdd);
                updateQuotationPrice(quotationToAdd);
            } else {
                throw new NotFoundException("Promotion not found in quotation");
            }
        } else {
            throw new NotFoundException("Quotation or promotion not found");
        }
    }

    public void updateQuotationPrice(Quotation quotationUpdate) {
        if (quotationUpdate.getCombo() != null) {
            ComboPrice comboPrice = comboPriceRepository.findByComboIdAndMinVolumeLessThanEqualAndMaxVolumeGreaterThanEqual(quotationUpdate.getCombo().getId(), quotationUpdate.getPondVolume(), quotationUpdate.getPondVolume());
            if (comboPrice != null) {
                quotationUpdate.setInitialPrice(comboPrice.getUnitPrice().multiply(BigDecimal.valueOf(quotationUpdate.getPondVolume())));
            } else {
                throw new NotFoundException("Combo or Combo Price not found");
            }

        }

        ////////////////////////////////

        Set<Promotion> promotionList = quotationUpdate.getPromotions();

        float totalDiscountPercentage = 0;

        for (Promotion promotion : promotionList) {
            totalDiscountPercentage+=promotion.getDiscountPercent();
        }

        BigDecimal value = new BigDecimal(Float.toString(totalDiscountPercentage));

        quotationUpdate.setDiscountPrice(quotationUpdate.getInitialPrice().multiply(value));

        quotationUpdate.setFinalPrice(quotationUpdate.getInitialPrice().subtract(quotationUpdate.getDiscountPrice()));

        ///////////////////

        quotationRepository.save(quotationUpdate);
    }

    public QuotationResponse updateQuotation(@PathVariable long id, QuotationRequest quotationRequest) {
        Optional<Quotation> quotation = quotationRepository.findById(id);
        if (quotation.isPresent()) {
            Quotation quotationToUpdate = quotation.get();
            Quotation quotationUpdate;
            quotationUpdate = modelMapper.map(quotationRequest, Quotation.class);
            quotationUpdate.setId(quotationToUpdate.getId());
            quotationUpdate.setConstructionOrder(quotationToUpdate.getConstructionOrder());
            quotationUpdate.setPromotions(quotationToUpdate.getPromotions());
            quotationUpdate.setStatus(quotationUpdate.getStatus());

            if (quotationUpdate.getStatus() == QuotationStatus.CUSTOMER_PENDING) {
                EmailPaymentDetail emailPaymentDetail = new EmailPaymentDetail();
                emailPaymentDetail.setReceiver(quotationUpdate.getConstructionOrder().getCustomer().getAccount());
                emailPaymentDetail.setSubject("[KoiPondCons] Thông Báo Thanh Toán Đợt 1 – Thiết Kế Hồ Cá Koi");
                emailPaymentDetail.setText1("Chúng tôi xin chân thành cảm ơn Quý khách đã tin tưởng và lựa chọn dịch vụ thiết kế hồ cá Koi của chúng tôi. Để thuận tiện cho việc triển khai công việc và đảm bảo tiến độ, chúng tôi xin phép nhắc Quý khách về đợt thanh toán đầu tiên.");
                emailPaymentDetail.setText2("Theo thỏa thuận, đợt thanh toán 1 là cần thiết để bắt đầu quá trình thiết kế. Do đó, chúng tôi mong Quý khách vui lòng hoàn tất thanh toán đợt đầu này trong thời gian sớm nhất có thể. Chi tiết thanh toán đã được gửi trong hợp đồng và bảng báo giá.");
                emailPaymentDetail.setText3("Quý khách có thể thanh toán qua chuyển khoản ngân hàng hoặc các phương thức thanh toán khác mà chúng tôi đã cung cấp. Sau khi nhận được thanh toán, chúng tôi sẽ khởi động dự án và đội ngũ thiết kế của chúng tôi sẽ nhanh chóng tiến hành các bước tiếp theo để mang đến cho Quý khách một hồ cá Koi đẹp mắt và ưng ý nhất.");
                emailPaymentDetail.setText4("Nếu có bất kỳ câu hỏi nào liên quan đến việc thanh toán hoặc các thông tin khác, vui lòng liên hệ với chúng tôi qua số điện thoại hoặc email dưới đây để được hỗ trợ kịp thời.");
                emailPaymentDetail.setText5("Trân trọng, KoiPondCons");
                emailService.sendFirstPaymentEmail(emailPaymentDetail);
            }

            if (quotationUpdate.getStatus() == QuotationStatus.CUSTOMER_CONFIRMED) {
                consOrderPaymentService.addConsOrderPayment(quotationUpdate.getConstructionOrder());
            }

            updateQuotationPrice(quotationUpdate);

            return getQuotationResponse(quotationUpdate);
        } else {
            throw new NotFoundException("Quotation not found");
        }
    }

    public QuotationResponse getQuotationResponse(Quotation quotation) {
        QuotationResponse quotationResponse = new QuotationResponse();
        quotationResponse.setId(quotation.getId());
        quotationResponse.setCombo(quotation.getCombo());
        quotationResponse.setPondVolume(quotation.getPondVolume());
        quotationResponse.setQuotationFile(quotation.getQuotationFile());
        quotationResponse.setPromotions(quotation.getPromotions());
        quotationResponse.setStatus(quotation.getStatus());
        quotationResponse.setStatusDescription(quotation.getStatus().getDescription());
        quotationResponse.setInitialPrice(quotation.getInitialPrice());
        quotationResponse.setDiscountPrice(quotation.getDiscountPrice());
        quotationResponse.setFinalPrice(quotation.getFinalPrice());
        return quotationResponse;
    }
}

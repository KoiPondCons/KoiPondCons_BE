package com.koiteampro.koipondcons.services;

import com.koiteampro.koipondcons.entities.ComboPrice;
import com.koiteampro.koipondcons.entities.Promotion;
import com.koiteampro.koipondcons.entities.Quotation;
import com.koiteampro.koipondcons.enums.QuotationStatus;
import com.koiteampro.koipondcons.exception.NotFoundException;
import com.koiteampro.koipondcons.models.request.QuotationRequest;
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
            ComboPrice comboPrice = comboPriceRepository.findByComboIdAndMinVolumeLessThanAndMaxVolumeGreaterThan(quotationUpdate.getCombo().getId(), quotationUpdate.getPondVolume(), quotationUpdate.getPondVolume());
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

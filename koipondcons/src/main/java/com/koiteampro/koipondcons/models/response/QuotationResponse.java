package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Combo;
import com.koiteampro.koipondcons.entities.Promotion;
import com.koiteampro.koipondcons.enums.QuotationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class QuotationResponse {
    private long id;
    private Combo combo;
    private float pondVolume;
    private String quotationFile;
    private BigDecimal initialPrice;
    private BigDecimal discountPrice;
    private BigDecimal finalPrice;
    private QuotationStatus status;
    private String statusDescription;
    private Set<Promotion> promotions;
}

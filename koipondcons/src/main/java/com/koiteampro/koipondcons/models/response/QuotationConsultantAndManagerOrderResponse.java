package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.QuotationStatus;
import lombok.Data;

@Data
public class QuotationConsultantAndManagerOrderResponse {

    private QuotationStatus status;

    private float pondVolume;

    private String statusDescription;
}

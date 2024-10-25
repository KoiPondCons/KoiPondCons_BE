package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Combo;
import com.koiteampro.koipondcons.enums.QuotationStatus;
import lombok.Data;

@Data
public class QuotationRequest {

    private Combo combo;

    private String quotationFile;

    private float pondVolume;

    private QuotationStatus status;
}

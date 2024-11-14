package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

@Data
public class ConstructionOrderConsultantResponse {

    private long id;

    private long customerAccountId;

    private ConstructionOrderStatus status;

    private String statusDescription;

    private String pondAddress;

    private String customerEmail;

    private String customerName;

    private String customerPhone;

    private String customerDescription;

    private QuotationConsultantAndManagerOrderResponse quotation;

    private DesignDrawingConsultantAndManagerOrderResponse designDrawing;

}

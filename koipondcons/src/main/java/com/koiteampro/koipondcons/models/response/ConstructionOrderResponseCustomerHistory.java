package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConstructionOrderResponseCustomerHistory {
    private long id;

    private StaffResponse consultantAccount;

    private ConstructionOrderStatus status;

    private String statusDescription;

//    private QuotationResponse quotationResponse;

    private QuotationHistoryResponse quotation;

//    private DesignDrawingResponse designDrawingResponse;

    private DesignDrawingHistoryResponse designDrawing;

    private String pondAddress;

    private boolean isDesigned;

    private LocalDateTime requestDate;
}

package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConstructionOrderManagerResponse {

    private long id;

    private ConstructionOrderStatus status;

    private String statusDescription;

    private String customerName;

    private String customerDescription;

    private QuotationConsultantAndManagerOrderResponse quotation;

    private Account constructorAccount;

    private DesignDrawingConsultantAndManagerOrderResponse designDrawing;

    private LocalDateTime requestDate;

}

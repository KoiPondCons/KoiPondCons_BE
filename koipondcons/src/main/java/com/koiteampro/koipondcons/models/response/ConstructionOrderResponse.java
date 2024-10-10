package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.entities.DesignDrawing;
import com.koiteampro.koipondcons.entities.Quotation;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConstructionOrderResponse {

    private long id;

    private Customer customer;

    private Account consultantAccount;

    private ConstructionOrderStatus status;

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String pondAddress;

    private boolean isDesigned;

    private LocalDateTime requestDate;

    private LocalDateTime confirmedDate;

    private String statusDescription;

    private String customerDescription;

    private QuotationResponse quotationResponse;

    private DesignDrawingResponse designDrawingResponse;

    private Account constructorAccount;

}

package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.*;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<ConsOrderPayment> consOrderPaymentList;

    private double constructionProgress = 0;
}

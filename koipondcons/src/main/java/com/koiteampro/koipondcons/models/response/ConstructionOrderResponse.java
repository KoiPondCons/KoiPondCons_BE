package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConstructionOrderResponse {

    private Customer customer;

    private ConstructionOrderStatus status;

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String pondAddress;

    private boolean isDesigned;

    private LocalDateTime requestDate;

    private LocalDateTime confirmedDate;
}

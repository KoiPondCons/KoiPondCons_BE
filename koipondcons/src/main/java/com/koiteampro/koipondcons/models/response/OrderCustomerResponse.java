package com.koiteampro.koipondcons.models.response;

import lombok.Data;

@Data
public class OrderCustomerResponse {
    private long orderId;
    private String customerName;
    private String customerPhone;
    private String pondAddress;
}

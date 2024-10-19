package com.koiteampro.koipondcons.models.response;

import lombok.Data;

@Data
public class SubmitPaymentResponse {
    private long orderId;
    private boolean status;
}

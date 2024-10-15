package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.enums.PaymentMethod;
import lombok.Data;

@Data
public class ConsOrderPaymentUpdateRequest {
    private boolean isPaid;
    private PaymentMethod paymentMethod;
}

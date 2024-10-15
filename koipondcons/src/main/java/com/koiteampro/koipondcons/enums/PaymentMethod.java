package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethod {
    CASH("Tiền mặt"),
    TRANSFER("Chuyển khoản");
    private String description;
}

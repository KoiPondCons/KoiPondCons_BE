package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    NOT_YET("Chưa thanh toán"),
    FINISHED("Đã thanh toán");

    private String description;


}

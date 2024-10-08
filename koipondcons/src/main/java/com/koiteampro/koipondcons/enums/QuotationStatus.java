package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuotationStatus {
    PROCESSING("Đang thương lượng");

    private String description;
}

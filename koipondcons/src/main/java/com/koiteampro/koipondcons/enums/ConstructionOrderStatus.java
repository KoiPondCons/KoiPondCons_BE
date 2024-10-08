package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionOrderStatus {
    REQUESTED("Chờ tư vấn"),
    PROCESSING("Đang tư vấn"),
    DESIGNING("Đang thiết kế"),
    DESIGNED("Đã có bản thiết kế"),
    CONSTRUCTING("Đang thi công"),
    CONSTRUCTED("Chờ nghiệm thu và bàn giao"),
    FINISHED("Đã hoàn tất thanh toán"),
    CLOSED("Dự án đã hoàn thành"),
    CANCELED("Đã hủy đơn")
    ;
    private String description;
}

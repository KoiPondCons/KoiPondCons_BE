package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstructionOrderStatus {
    REQUESTED("Chờ tư vấn"),
    PROCESSING("Đang tư vấn"),
    CONFIRMED("Đơn hàng đã tạo, chờ thanh toán đợt 1"),
    DESIGNING("Đang thiết kế"),
    PAYMENT_PHASE_2_PENDING("Chờ thanh toán đợt 2"),
    CONSTRUCTING("Đang thi công"),
    DELIVER_PENDING("Chờ nghiệm thu và bàn giao"),
    PAYMENT_FINAL_PENDING("Chờ hoàn tất thanh toán"),
    COMPLETED("Đã hoàn thành"),
    CANCELED("Đã hủy đơn")
    ;
    private String description;
}

package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MaintenanceOrderStatus {
    REQUESTED("Chờ tư vấn"),
    PENDING("Chờ xác nhận"),
    PROCESSING("Đang thực hiện bảo trì"),
    PROCESSED("Chờ thanh toán"),
    FINISHED("Đã hoàn tất"),
    CANCELED("Đã hủy đơn");

    private String description;
}

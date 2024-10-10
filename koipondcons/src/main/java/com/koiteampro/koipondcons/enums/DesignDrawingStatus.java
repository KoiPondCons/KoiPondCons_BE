package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DesignDrawingStatus {
    DESIGNING("Đang thiết kế"),
    MANAGER_PENDING("Chờ duyệt"),
    MANAGER_REJECTED("Quản lý từ chối"),
    CUSTOMER_PENDING("Chờ khách xác nhận"),
    CUSTOMER_CONFIRMED("Đã hoàn tất"),
    CUSTOMER_REJECTED("Khách hàng từ chối");

    private String description;
}



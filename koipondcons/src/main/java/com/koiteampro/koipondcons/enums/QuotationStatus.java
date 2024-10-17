package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QuotationStatus {
    PROCESSING("Đang xử lý"),
    MANAGER_PENDING("Chờ duyệt"),
    MANAGER_REJECTED("Quản lý từ chối"),
    CUSTOMER_PENDING("Chờ khách xác nhận"),
    CUSTOMER_CONFIRMED("Đã xác nhận"),
    CUSTOMER_REJECTED("Khách hàng từ chối"),
    CANCELED("Đã hủy");

    private String description;
}

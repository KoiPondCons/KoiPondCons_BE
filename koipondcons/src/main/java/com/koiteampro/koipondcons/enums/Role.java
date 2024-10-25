package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    CUSTOMER("Khách hàng"),
    CONSULTANT("Tư vấn viên"),
    DESIGNER("Thiết kế viên"),
    CONSTRUCTOR("Trưởng nhóm xây dựng"),
    MANAGER("Quản lý")
    ;
    private String description;
}

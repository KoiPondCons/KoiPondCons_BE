package com.koiteampro.koipondcons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DesignDrawingStatus {
    NOTSTART("Chưa bắt đầu"),
    PROCESSING("Đang thiết kế");

    private String description;
}



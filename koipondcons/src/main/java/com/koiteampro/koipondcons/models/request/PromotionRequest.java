package com.koiteampro.koipondcons.models.request;

import lombok.Data;

@Data
public class PromotionRequest {
    private String content;
    private float discountPercent;
    private int pointsAvailable;
}

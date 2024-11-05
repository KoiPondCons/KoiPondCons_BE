package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import lombok.Data;

@Data
public class DesignDrawingConsultantOrderResponse {
    private DesignDrawingStatus status;
    private String statusDescription;
}

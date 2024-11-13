package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import lombok.Data;

@Data
public class DesignDrawingConsultantAndManagerOrderResponse {
    private DesignDrawingStatus status;
    private String statusDescription;
}

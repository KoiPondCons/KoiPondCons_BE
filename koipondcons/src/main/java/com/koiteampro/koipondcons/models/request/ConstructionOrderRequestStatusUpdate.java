package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import lombok.Data;

@Data
public class ConstructionOrderRequestStatusUpdate {

    private ConstructionOrderStatus status;
}

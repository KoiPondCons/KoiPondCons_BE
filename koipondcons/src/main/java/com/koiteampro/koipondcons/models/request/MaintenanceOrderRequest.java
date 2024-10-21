package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import lombok.Data;

@Data
public class MaintenanceOrderRequest {

    private boolean isWarranted;

    private String customerName;

    private String customerPhone;

    private String pondAddress;

    private float pondVolume;
}

package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.entities.Customer;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class MaintenanceOrderRequest {

    private boolean isWarranted;

    private String customerName;

    private String customerPhone;

    private String pondAddress;

    private float pondVolume;

    private MaintenanceOrderStatus status;
}

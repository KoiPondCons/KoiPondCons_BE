package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceOrderUpdateRequest {
    private Account consultantAccount;

    private Account constructorAccount;

    private boolean isWarranted;

    private float pondVolume;

    private double price;

    private LocalDate dateEnd;

    private MaintenanceOrderStatus status;

}

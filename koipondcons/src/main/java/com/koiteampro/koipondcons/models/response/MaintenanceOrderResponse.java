package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceOrderResponse {

    private String customerName;

    private String customerPhone;

    private String pondAddress;

    private float pondVolume;

    private boolean isWarranted;

    private double price;

    private String consultantName;

    private String consultantPhone;

    private String constructorName;

    private LocalDate endDate;

    private LocalDate createAt;

    private MaintenanceOrderStatus status;

}

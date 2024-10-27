package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceOrderResponse {
    private long id;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String pondAddress;

    private float pondVolume;

    private String customerDescription;

    private boolean isWarranted;

    private double price;

    private String consultantName;

    private String consultantPhone;

    private String constructorName;

    private LocalDate endDate;

    private LocalDate createAt;

    private MaintenanceOrderStatus status;

    private String statusDescription;
}

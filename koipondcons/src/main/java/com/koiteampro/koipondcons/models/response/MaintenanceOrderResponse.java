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

    private double price;

    private Account consultantAccount;

    private Account constructorAccount;

    private LocalDate endDate;

    private LocalDate createAt;

    private MaintenanceOrderStatus status;

}

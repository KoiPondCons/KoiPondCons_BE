package com.koiteampro.koipondcons.entities;

import com.koiteampro.koipondcons.enums.MaintenanceOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class MaintenanceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "consultant_account_id")
    private Account consultantAccount;

    @ManyToOne
    @JoinColumn(name = "constructor_account_id")
    private Account constructorAccount;

    private boolean isWarranted;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String customerName;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    private String customerPhone;

    private String pondAddress;

    private float pondVolume;

    private double price;

    private LocalDate endDate;

    private LocalDate createAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaintenanceOrderStatus status = MaintenanceOrderStatus.REQUESTED;
}

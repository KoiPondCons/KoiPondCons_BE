package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class ConstructionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConstructionOrderStatus status = ConstructionOrderStatus.REQUESTED;

    @ManyToOne
    @JoinColumn(name = "consultant_account_id", nullable = true)
    private Account consultantAccount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String customerName;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    private String customerPhone;

    @Email(message = "Không đúng định dạng email!")
    private String customerEmail;

    private String pondAddress;

    private String customerDescription;

//    private float pondVolume;

    private boolean isDesigned;

    private LocalDateTime requestDate = LocalDateTime.now();

    private LocalDateTime confirmedDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "combo_id")
//    private Combo combo;

    @OneToOne(mappedBy = "constructionOrder", cascade = CascadeType.PERSIST)
    private DesignDrawing designDrawing;

    @OneToMany(mappedBy = "constructionOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StaffConstructionDetail> staffConstructionDetailList;

    @OneToMany(mappedBy = "constructionOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConsOrderPayment> consOrderPaymentList;

    @OneToOne(mappedBy = "constructionOrder", cascade = CascadeType.PERSIST)
    private Quotation quotation;
}

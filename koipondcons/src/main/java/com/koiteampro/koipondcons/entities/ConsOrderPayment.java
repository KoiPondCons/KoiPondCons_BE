package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class ConsOrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_order_id", nullable = false)
    @JsonIgnore
    private ConstructionOrder constructionOrder;

    @Column(nullable = false)
    private int period;


    private LocalDateTime paidAt;

    @Column(nullable = false)
    private BigDecimal amount;

    private String content;

    private boolean isPaid = false;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = null;
}

package com.koiteampro.koipondcons.entities;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_order_id", nullable = false)
    private ConstructionOrder constructionOrder;

    @Column(nullable = false)
    private int period;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    @Column(nullable = false)
    private BigDecimal amount;

    private String content;

    private boolean isPaid = false;

    private PaymentMethod paymentMethod;
}

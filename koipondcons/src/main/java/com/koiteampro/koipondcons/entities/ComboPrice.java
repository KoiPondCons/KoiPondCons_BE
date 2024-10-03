package com.koiteampro.koipondcons.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class ComboPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo combo;

    @Column(nullable = false)
    private float minVolume;

    @Column(nullable = false)
    private float maxVolume;

    @Column(nullable = false)
    private BigDecimal unitPrice;
}

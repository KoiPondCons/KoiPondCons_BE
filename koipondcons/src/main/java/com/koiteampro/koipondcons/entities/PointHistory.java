package com.koiteampro.koipondcons.entities;

import com.koiteampro.koipondcons.enums.PointAction;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Entity
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private double points;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PointAction pointAction = PointAction.ADD;

    private String content;

    private LocalDateTime createAt;
}

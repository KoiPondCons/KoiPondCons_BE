package com.koiteampro.koipondcons.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sendAt;

    private boolean isRead = false;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    Account account;
}

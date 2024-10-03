package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DesignDrawing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "construction_order_id", unique = true, nullable = false)
    @JsonIgnore
    private ConstructionOrder constructionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designer_account_id")
    private Account designerAccount;

    @Column(nullable = false)
    private String designFile;

    private boolean isConfirmed = false;
}

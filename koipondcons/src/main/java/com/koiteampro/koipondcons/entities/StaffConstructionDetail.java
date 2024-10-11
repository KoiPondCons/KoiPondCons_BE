package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class StaffConstructionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_order_id", nullable = false)
    @JsonIgnore
    private ConstructionOrder constructionOrder;

    @ManyToOne
    @JoinColumn(name = "constructor_account_id", nullable = false)
    private Account constructorAccount;

    @ManyToOne
    @JoinColumn(name = "construction_item_id", nullable = false)
    private ComboConstructionItem constructionItem;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private boolean isFinished = false;
}

package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.enums.PointAction;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int total_points;

    public Customer(long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = false)
    Account account;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PointHistory> pointHistoryList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConstructionOrder> constructionOrderList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<MaintenanceOrder> maintenanceOrderList;
}

package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.enums.QuotationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "construction_order_id", unique = true, nullable = false)
    @JsonIgnore
    private ConstructionOrder constructionOrder;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;

    private float pondVolume = 0;

    private String quotationFile;

    private BigDecimal initialPrice = BigDecimal.ZERO;

    private BigDecimal discountPrice = BigDecimal.ZERO;

    private BigDecimal finalPrice = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuotationStatus status = QuotationStatus.PROCESSING;

//    @OneToMany(mappedBy = "quotation", fetch = FetchType.LAZY)
//    private List<QuotationPromotion> quotationPromotionList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "quotation_promotion", joinColumns = @JoinColumn(name = "quotation_id"), inverseJoinColumns = @JoinColumn(name = "promotion_id"))
    @JsonIgnore
    private Set<Promotion> promotions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quotation that = (Quotation) o;
        return id == that.id;
    }
}

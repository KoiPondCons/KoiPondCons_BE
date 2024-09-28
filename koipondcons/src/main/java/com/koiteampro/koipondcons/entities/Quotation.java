package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Quotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "construction_order_id", unique = true, nullable = false)
    @JsonIgnore
    private ConstructionOrder constructionOrder;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    private Combo combo;

    private String quotationFile;

    private BigDecimal initialPrice;

    private BigDecimal discountPrice;

    private BigDecimal finalPrice;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL)
    private List<QuotationPromotion> quotationPromotionList;

}

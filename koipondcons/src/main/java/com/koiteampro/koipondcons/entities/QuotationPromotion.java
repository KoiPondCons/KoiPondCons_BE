package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
//@Entity
public class QuotationPromotion {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "quotation_id")
//    @JsonIgnore
//    private Quotation quotation;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "promotion_id")
//    private Promotion promotion;
}

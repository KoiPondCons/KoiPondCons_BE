package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ComboPrice> comboPriceList;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ComboConstructionItem> comboConstructionItemList;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Quotation> quotationList;
}

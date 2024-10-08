package com.koiteampro.koipondcons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ComboConstructionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne(fetch = FetchType.LAZY) /////////
    @ManyToOne
    @JoinColumn(name = "combo_id", nullable = false)
//    @JsonIgnore
    private Combo combo;

    @Column(nullable = false)
    private String itemContent;

    private int duration;

    @OneToMany(mappedBy = "constructionItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<StaffConstructionDetail> staffConstructionDetailList;
}

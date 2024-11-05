package com.koiteampro.koipondcons.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.entities.Combo;
import com.koiteampro.koipondcons.entities.StaffConstructionDetail;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class ComboConstructionItemResponse {

    private long id;

    private String itemContent;

    private int duration;

    private boolean isDisabled = false;

    private List<StaffConstructionDetail> staffConstructionDetailList;
}

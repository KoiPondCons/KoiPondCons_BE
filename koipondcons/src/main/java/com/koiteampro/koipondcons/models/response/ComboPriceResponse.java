package com.koiteampro.koipondcons.models.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.entities.Combo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComboPriceResponse {

    private long id;


    private float minVolume;

    private float maxVolume;

    private BigDecimal unitPrice;

    private boolean isDisabled = false;
}

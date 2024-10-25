package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Combo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ComboPriceRequest {

    private Combo combo;

    private float minVolume;

    private float maxVolume;

    private BigDecimal unitPrice;
}

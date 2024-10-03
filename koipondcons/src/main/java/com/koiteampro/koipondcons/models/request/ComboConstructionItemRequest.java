package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Combo;
import lombok.Data;

@Data
public class ComboConstructionItemRequest {
    private Combo combo;

    private String itemContent;

    private int duration;
}

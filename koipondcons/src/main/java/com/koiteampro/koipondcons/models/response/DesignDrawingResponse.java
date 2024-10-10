package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import lombok.Data;

@Data
public class DesignDrawingResponse {
    private long id;
    private ConstructionOrder constructionOrder;
    private String designFile;
    private DesignDrawingStatus status;
    private String statusDescription;
}

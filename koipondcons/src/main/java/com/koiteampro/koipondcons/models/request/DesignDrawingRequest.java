package com.koiteampro.koipondcons.models.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.koiteampro.koipondcons.entities.Account;
import com.koiteampro.koipondcons.entities.ConstructionOrder;
import com.koiteampro.koipondcons.enums.DesignDrawingStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class DesignDrawingRequest {

    private Account designerAccount;

    private String designFile;

    private DesignDrawingStatus status;
}

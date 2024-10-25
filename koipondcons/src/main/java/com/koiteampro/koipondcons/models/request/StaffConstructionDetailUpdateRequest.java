package com.koiteampro.koipondcons.models.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffConstructionDetailUpdateRequest {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private boolean isFinished;
}

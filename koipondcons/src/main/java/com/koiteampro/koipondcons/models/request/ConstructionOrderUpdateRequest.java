package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.enums.ConstructionOrderStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConstructionOrderUpdateRequest {

    private ConstructionOrderStatus status = ConstructionOrderStatus.REQUESTED;

    private String customerName;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    private String customerPhone;

    @Email(message = "Không đúng định dạng email!")
    private String customerEmail;

    private String pondAddress;

    private boolean isDesigned;

    private LocalDateTime confirmedDate;

    private LocalDate warrantyEndDate;

    private int warrantyRemaining;
}

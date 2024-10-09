package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.entities.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ConstructionOrderRequest {

    private String customerName;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    private String customerPhone;

    @Email(message = "Không đúng định dạng email!")
    private String customerEmail;

    private String pondAddress;

    private float pondVolume;

    private boolean isDesigned;
}

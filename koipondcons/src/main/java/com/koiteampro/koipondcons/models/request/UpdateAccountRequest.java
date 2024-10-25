package com.koiteampro.koipondcons.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateAccountRequest
{
    @NotBlank(message = "Tên không để trống")
    private String name;

    @NotBlank(message = "Email không để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String address;

    private String avatar;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    private String phone;

}

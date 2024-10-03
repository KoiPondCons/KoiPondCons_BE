package com.koiteampro.koipondcons.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Vui lòng nhập email!")
    @Email(message = "Không đúng định dạng email!")
    String email;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    String password;
}

package com.koiteampro.koipondcons.model;

import com.koiteampro.koipondcons.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Vui lòng nhập họ và tên!")
    String name;

    @NotBlank(message = "Vui lòng nhập email!")
    @Email(message = "Không đúng định dạng email!")
    String email;

    @Pattern(regexp = "^(84|0)+[3|5|7|8|9]\\d{8}$", message = "Số điện thoại không hợp lệ!")
    String phone;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, message = "Password must be at least 6 characters!")
    String password;

    Role role;
}

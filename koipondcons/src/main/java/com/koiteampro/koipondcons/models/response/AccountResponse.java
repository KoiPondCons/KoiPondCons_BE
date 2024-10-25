package com.koiteampro.koipondcons.models.response;

import com.koiteampro.koipondcons.enums.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountResponse {
    long id;
    String name;
    String avatar;
    String email;
    String phone;
    String address;
    Role role;
    LocalDate dateCreate;
    String token;
}

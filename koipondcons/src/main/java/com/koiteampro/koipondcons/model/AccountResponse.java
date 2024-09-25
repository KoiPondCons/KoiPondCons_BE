package com.koiteampro.koipondcons.model;

import com.koiteampro.koipondcons.entity.Role;
import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String name;
    String avatar;
    String email;
    String phone;
    String address;
    Role role;
    String token;
}

package com.koiteampro.koipondcons.models.request;

import com.koiteampro.koipondcons.enums.Role;

public class SetRoleRequest {
    private Role role;

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}

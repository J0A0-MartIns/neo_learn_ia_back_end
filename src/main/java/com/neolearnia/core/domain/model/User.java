package com.neolearnia.core.domain.model;

import com.neolearnia.infrastructure.adapters.inbound.http.dto.UserDto;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(UserDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
    }
}

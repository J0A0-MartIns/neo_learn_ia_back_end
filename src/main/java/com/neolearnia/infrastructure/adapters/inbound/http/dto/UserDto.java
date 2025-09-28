package com.neolearnia.infrastructure.adapters.inbound.http.dto;

import java.util.UUID;

public class UserDto {
    private String name;
    private String password;
    private String email;
    private UUID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {}

    public UserDto(UUID id, String name, String email, String password) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.id = id;
    }
}

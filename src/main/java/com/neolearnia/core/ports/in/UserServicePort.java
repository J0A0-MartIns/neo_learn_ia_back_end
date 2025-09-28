package com.neolearnia.core.ports.in;

import com.neolearnia.infrastructure.adapters.inbound.http.dto.UserDto;

public interface UserServicePort {
    void save(UserDto user);
}

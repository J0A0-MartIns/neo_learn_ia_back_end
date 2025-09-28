package com.neolearnia.core.service;

import com.neolearnia.core.domain.model.User;
import com.neolearnia.core.ports.in.UserServicePort;
import com.neolearnia.core.ports.out.UserRepositoryPort;
import com.neolearnia.infrastructure.adapters.inbound.http.dto.UserDto;

public class UserServiceImp implements UserServicePort {

    private final UserRepositoryPort userRepository;

    public UserServiceImp(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User(userDto);
        this.userRepository.save(user);
    }
}

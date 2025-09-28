package com.neolearnia.infrastructure.adapters.inbound.http.controller;


import com.neolearnia.core.ports.in.UserServicePort;
import com.neolearnia.infrastructure.adapters.inbound.http.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private  final UserServicePort userServicePort;

    public UserController(UserServicePort userServicePort) {
        this.userServicePort = userServicePort;
    }

    @PostMapping
    void create(@RequestBody UserDto userDto) {
        userServicePort.save(userDto);
    }
}

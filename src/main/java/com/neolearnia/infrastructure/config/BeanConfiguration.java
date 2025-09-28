package com.neolearnia.infrastructure.config;

import com.neolearnia.core.ports.in.UserServicePort;
import com.neolearnia.core.ports.out.UserRepositoryPort;
import com.neolearnia.core.service.UserServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    UserServicePort userServicePort(UserRepositoryPort userRepositoryPort){
        return new UserServiceImp(userRepositoryPort);
    }
}

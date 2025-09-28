package com.neolearnia.infrastructure.adapters.outbound.persistence.jpa.repository;

import com.neolearnia.core.domain.model.User;
import com.neolearnia.core.ports.out.UserRepositoryPort;
import com.neolearnia.infrastructure.adapters.outbound.persistence.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringUserRepository springUserRepository;

    public UserRepositoryAdapter(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }

    @Override
    public void save(User user){
        UserEntity userEntity = new UserEntity(user);
        this.springUserRepository.save(userEntity);
    }
}

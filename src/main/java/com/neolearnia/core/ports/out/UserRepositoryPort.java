package com.neolearnia.core.ports.out;

import com.neolearnia.core.domain.model.User;

public interface UserRepositoryPort {

    void save(User user);
}

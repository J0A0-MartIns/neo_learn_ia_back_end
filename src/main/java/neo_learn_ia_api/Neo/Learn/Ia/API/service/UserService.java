package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;

import java.util.List;

public interface UserService {


    void createUser(CreateUserDto createUserDto);
    List<User> listUsers();
}

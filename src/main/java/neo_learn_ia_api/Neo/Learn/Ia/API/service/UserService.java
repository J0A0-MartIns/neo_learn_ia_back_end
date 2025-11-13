package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UpdateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UserDto;

import java.util.List;

public interface UserService {


    void createUser(CreateUserDto createUserDto);
    List<User> listUsers();

    UserDto getUserProfile(Long userId);
    void updateUser(Long userId, UpdateUserDto updateUserDto);
}

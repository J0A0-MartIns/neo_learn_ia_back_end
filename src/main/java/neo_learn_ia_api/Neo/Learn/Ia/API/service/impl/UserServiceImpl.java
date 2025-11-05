package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import jakarta.transaction.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.UserMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.RoleRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.UserRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.UserService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.helpers.Helpers;
import neo_learn_ia_api.Neo.Learn.Ia.API.validation.DomainValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public void createUser(CreateUserDto createUserDto) {
        var basicRole = roleRepository.findByName(User.Values.BASIC.name());

        userRepository.findByUserEmail(createUserDto.userEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already exists");
                });
        User user = userMapper.toEntity(createUserDto, passwordEncoder, basicRole);
        DomainValidator.validateOrThrow(user);
        userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}

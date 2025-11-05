package neo_learn_ia_api.Neo.Learn.Ia.API.mapper;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.Role;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    @Mapping(target = "roles", expression = "java(java.util.Set.of(basicRole))")
    User toEntity(CreateUserDto dto, PasswordEncoder passwordEncoder, Role basicRole);
}
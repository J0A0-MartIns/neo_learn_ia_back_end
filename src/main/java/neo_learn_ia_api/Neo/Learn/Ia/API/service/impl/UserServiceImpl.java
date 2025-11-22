package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import jakarta.transaction.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.UserMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.User;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.VerificationToken;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.RoleRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.UserRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.VerificationTokenRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.EmailService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.UserService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.helpers.Helpers;
import neo_learn_ia_api.Neo.Learn.Ia.API.validation.DomainValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UpdateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.UserDto;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,UserMapper userMapper, VerificationTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
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

        user.setEmailVerificado(false);

        DomainValidator.validateOrThrow(user);
        User savedUser = userRepository.save(user);

        createTokenAndSendEmail(savedUser, VerificationToken.TokenType.EMAIL_VERIFICATION);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserDto(
                user.getId(),
                user.getUserEmail(),
                user.getUserFirstName(),
                user.getTelefone(),
                user.getCargo(),
                user.getInstituicao()
        );
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UpdateUserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setUserFirstName(dto.userFirstName());
        user.setTelefone(dto.telefone());
        user.setCargo(dto.cargo());
        user.setInstituicao(dto.instituicao());

        userRepository.save(user);
    }

    private void createTokenAndSendEmail(User user, VerificationToken.TokenType type) {
        tokenRepository.deleteByUserIdAndType(user.getId(), type);
        int minutes = type == VerificationToken.TokenType.EMAIL_VERIFICATION ? 1440 : 30;
        VerificationToken token = new VerificationToken(user, type, minutes);
        tokenRepository.save(token);
        if (type == VerificationToken.TokenType.EMAIL_VERIFICATION) {
            emailService.sendConfirmationEmail(user.getUserEmail(), token.getToken());
        } else {
            emailService.sendPasswordResetEmail(user.getUserEmail(), token.getToken());
        }
    }

    @Override
    @Transactional
    public void updateEmail(Long userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!user.getUserEmail().equals(newEmail)) {
            if(userRepository.findByUserEmail(newEmail).isPresent()){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email já está em uso");
            }
            user.setUserEmail(newEmail);
            user.setEmailVerificado(false);
            userRepository.save(user);

            createTokenAndSendEmail(user, VerificationToken.TokenType.EMAIL_VERIFICATION);
        }
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        VerificationToken vToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        System.out.println("Data Agora: " + Instant.now());
        System.out.println("Data Expiração Token: " + vToken.getExpiryDate());

        if (vToken.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado");
        }

        User user = vToken.getUser();

        if (user.isEmailVerificado()) {
            tokenRepository.delete(vToken);
            return;
        }

        user.setEmailVerificado(true);
        userRepository.save(user);
        tokenRepository.delete(vToken);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        createTokenAndSendEmail(user, VerificationToken.TokenType.PASSWORD_RESET);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        VerificationToken vToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido"));

        if (vToken.isExpired() || vToken.getType() != VerificationToken.TokenType.PASSWORD_RESET) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expirado ou inválido");
        }

        User user = vToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(vToken);
    }
}

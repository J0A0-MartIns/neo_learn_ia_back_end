package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.Role;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.UserRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.LoginService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public LoginServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        var user = userRepository.findByUserEmail(loginRequest.userEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!user.isLoginCorrect(loginRequest, bCryptPasswordEncoder)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var now = Instant.now();
        var expiresIn = 3600L;

        var scopes = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("myBack")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, expiresIn);
    }
}
package neo_learn_ia.service;

import lombok.RequiredArgsConstructor;
import neo_learn_ia.dto.LoginRequest;
import neo_learn_ia.dto.LoginResponse;
import neo_learn_ia.model.User;
import neo_learn_ia.repository.UserRepository;
import neo_learn_ia.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(token);
    }
}

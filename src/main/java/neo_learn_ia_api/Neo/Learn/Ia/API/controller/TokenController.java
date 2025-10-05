package neo_learn_ia_api.Neo.Learn.Ia.API.controller;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class TokenController {

    private final LoginService loginService;

    public TokenController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var loginResponse = loginService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }
}


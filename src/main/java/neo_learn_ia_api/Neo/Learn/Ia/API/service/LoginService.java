package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest loginRequest);
}
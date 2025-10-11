package neo_learn_ia_api.Neo.Learn.Ia.API.service.helpers;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class Helpers {

    public void validationName(String name)  {
        if(name==null || name.isEmpty()){
            throw  new IllegalArgumentException("O nome não pode ser nulo ou vazio") ;
        }
    }


    public void validationEmail(String email) {
        if(email==null || email.isEmpty()){
            throw  new IllegalArgumentException("O email não pode nulo ou vazio") ;
        }
    }

    public void validationPassword(String password) {
        if(password==null || password.isEmpty()){
            throw  new IllegalArgumentException("A senha não pode ser nulo ou vazia") ;
        }
    }

    public void validationCreateUser(CreateUserDto createUserDto) {
        validationEmail(createUserDto.userEmail());
        validationPassword(createUserDto.password());
    }

}

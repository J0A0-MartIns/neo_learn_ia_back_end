package neo_learn_ia_api.Neo.Learn.Ia.API.service.helpers;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateUserDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;

@Service
public class Helpers {

    private final StudyProjectRepository studyProjectRepository;

    public Helpers(StudyProjectRepository studyProjectRepository) {
        this.studyProjectRepository = studyProjectRepository;
    }

    public void validationName(String name)  {
        if(name==null || name.isEmpty()){
            throw  new IllegalArgumentException("O nome não pode ser nulo ou vazio") ;
        }
        boolean nameExists = studyProjectRepository.existsByNameIgnoreCase(name);
        if (nameExists) {
            throw new IllegalArgumentException("Já existe um projeto com o nome: " + name);
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

    public  void copyFields(Object source, Object target) {
        if (source == null || target == null) return;

        for (Field sourceField : source.getClass().getDeclaredFields()) {
            sourceField.setAccessible(true);
            try {
                Object value = sourceField.get(source);

                try {
                    Field targetField = target.getClass().getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                } catch (NoSuchFieldException ignored) {
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}

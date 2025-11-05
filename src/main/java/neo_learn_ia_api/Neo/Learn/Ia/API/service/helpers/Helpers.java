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

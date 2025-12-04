package neo_learn_ia_api.Neo.Learn.Ia.API.service;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.GenericService;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface StudyProjectService extends GenericService<
        Long,                   // Tipo do ID
        CreateStudyProjectDto,  // DTO de Input (I)
        StudyProjectResponseDto // DTO de Response (R)
        > {

    @Override
    StudyProjectResponseDto create(CreateStudyProjectDto dto) ;

    @Override
    StudyProjectResponseDto update(Long id, CreateStudyProjectDto dto);


    void deleteFileFromProject(Long id, Long fileId);
}

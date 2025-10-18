package neo_learn_ia_api.Neo.Learn.Ia.API.service;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface StudyProjectService {
    StudyProject save(CreateStudyProjectDto dto) throws IOException;

    StudyProjectResponseDto getStudyProjectById(Long id);
    List<StudyProjectResponseDto> getAllStudyProject();
    void deleteStudyProjectById(Long id);
    void updateStudyProject(Long id, CreateStudyProjectDto dto) throws IOException;
    void deleteFileFromProject(Long id, Long fileId);

}

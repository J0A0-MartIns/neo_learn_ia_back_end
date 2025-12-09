package neo_learn_ia_api.Neo.Learn.Ia.API.service;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ProjectsForSheduleResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.GenericService;

import java.util.List;

public interface StudyProjectService extends GenericService<
        Long,                   // Tipo do ID
        CreateStudyProjectDto,  // DTO de Input (I)
        StudyProjectResponseDto // DTO de Response (R)
        > {

    @Override
    StudyProjectResponseDto create(CreateStudyProjectDto dto) ;

    StudyProjectResponseDto create(CreateStudyProjectDto dto, Long ownerId);

    @Override
    StudyProjectResponseDto update(Long id, CreateStudyProjectDto dto);


    void deleteFileFromProject(Long id, Long fileId);

    @Override
    List<StudyProjectResponseDto> findAll();

    StudyProjectResponseDto publish(Long id);

    StudyProjectResponseDto unpublish(Long id);

    List<StudyProjectResponseDto> findPublicLibrary(Long currentUserId);

    StudyProjectResponseDto duplicate(Long id, Long newOwnerId);

    List<StudyProjectResponseDto> findByOwner(Long ownerId);

    List<ProjectsForSheduleResponse> getProjectsForShedule(Long userId);

    Long countClones(Long projectId);
}
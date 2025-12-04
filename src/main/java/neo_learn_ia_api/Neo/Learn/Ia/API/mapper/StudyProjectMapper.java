package neo_learn_ia_api.Neo.Learn.Ia.API.mapper;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.FileMetadataDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ProjectsForSheduleResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudyProjectMapper {

    @Mapping(target = "originalProjectId", source = "originalProjectId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "ownerName", source = "owner.userFirstName")
    @Mapping(target = "ownerId", source = "owner.id")
    StudyProjectResponseDto toResponseDTO(StudyProject entity);

    List<StudyProjectResponseDto> toResponseDTOList(List<StudyProject> entities);

    FileMetadataDto fileEntityToFileMetadataDto(FileEntity fileEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    StudyProject toEntity(CreateStudyProjectDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    void updateEntityFromDTO(@MappingTarget StudyProject entity, CreateStudyProjectDto dto);

    @Mappings({
            @Mapping(target = "studyProject", source = "studyProjectId"),
            @Mapping(target = "studyProjectName", source = "studyProjectName"),
            @Mapping(target = "fileId", source = "file.id"),
            @Mapping(target = "fileName", source = "file.fileName")
    })
    ProjectsForSheduleResponse toResponse(FileEntity file, Long studyProjectId, String studyProjectName);
}
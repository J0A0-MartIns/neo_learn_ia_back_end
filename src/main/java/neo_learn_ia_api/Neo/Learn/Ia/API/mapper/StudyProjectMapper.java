package neo_learn_ia_api.Neo.Learn.Ia.API.mapper;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.FileMetadataDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StudyProjectMapper {


    @Mapping(source = "owner.id", target = "ownerId")
    StudyProjectResponseDto toResponseDTO(StudyProject entity);

    List<StudyProjectResponseDto> toResponseDTOList(List<StudyProject> entities);

    FileMetadataDto fileEntityToFileMetadataDto(FileEntity fileEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    StudyProject toEntity(CreateStudyProjectDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachments", ignore = true)
    void updateEntityFromDTO(@MappingTarget StudyProject entity, CreateStudyProjectDto dto);
}
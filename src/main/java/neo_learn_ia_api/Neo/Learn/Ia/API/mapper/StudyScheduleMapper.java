package neo_learn_ia_api.Neo.Learn.Ia.API.mapper;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyScheduleMapper {

    @Mapping(source = "project.id", target = "projectId")
    StudyScheduleResponseDTO toDto(StudySchedule studySchedule);
}
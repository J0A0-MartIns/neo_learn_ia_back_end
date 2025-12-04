package neo_learn_ia_api.Neo.Learn.Ia.API.mapper;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleGetResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudyScheduleMapper {

    @Mapping(source = "project.id", target = "projectId")
    StudyScheduleResponseDTO toDto(StudySchedule studySchedule);

    List<ScheduleGetResponse> toListDto(List<StudySchedule> studySchedules);
    @Mapping(source = "project.name", target = "projectName")
    ScheduleGetResponse toDtoList(StudySchedule studySchedule);
}
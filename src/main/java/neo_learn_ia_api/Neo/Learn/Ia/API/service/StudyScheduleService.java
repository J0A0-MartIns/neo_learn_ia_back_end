package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleGetResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;

import java.util.List;

public interface StudyScheduleService {
    StudyScheduleResponseDTO getScheduleById(Long id);
    List<ScheduleGetResponse> getAllSchedule();
    void deleteScheduleById(Long id);
}

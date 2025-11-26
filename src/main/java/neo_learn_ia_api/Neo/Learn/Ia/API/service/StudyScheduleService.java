package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyScheduleResponseDTO;

public interface StudyScheduleService {
    StudyScheduleResponseDTO getScheduleById(Long id);

}

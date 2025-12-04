package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.util.Map;

public record StudyScheduleResponseDTO(Long id,
                                       String userId,
                                       Long projectId,
                                       Map<String, Object> scheduleData) {
}

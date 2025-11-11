package neo_learn_ia_api.Neo.Learn.Ia.API.dto;


public record ScheduleRequest(
        Long studyProjectId,
        Long fileId,
        Integer studyTimePerDay
) {
}

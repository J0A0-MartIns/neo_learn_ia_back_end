package neo_learn_ia_api.Neo.Learn.Ia.API.dto;


import java.util.List;

public record QuizPdfRequestDto(Long fileId,
                                Long projectId,
                                List<QuestionContent> questions) {
}

package neo_learn_ia_api.Neo.Learn.Ia.API.dto;


import java.util.List;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;

public record MultipleChoiceQuizResponse(
        String question,
        List<String> options,
        String answer
) {
    public static MultipleChoiceQuizResponse fromEntity(MultipleChoiceQuestionEntity entity) {
        return new MultipleChoiceQuizResponse(
                entity.getQuestion(),
                entity.getOptions(),
                entity.getAnswer()
        );
    }
}

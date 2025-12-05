package neo_learn_ia_api.Neo.Learn.Ia.API.dto;


import java.util.List;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;

public record MultipleChoiceQuizResponse(
        Long id,
        String question,
        List<String> options,
        String answer
) {
    public static MultipleChoiceQuizResponse fromEntity(MultipleChoiceQuestionEntity entity) {
        var content = entity.getData();

        return new MultipleChoiceQuizResponse(
                entity.getId(),
                content != null ? content.question() : null,
                content != null ? content.options() : List.of(),
                content != null ? content.answer() : null
        );
    }
}

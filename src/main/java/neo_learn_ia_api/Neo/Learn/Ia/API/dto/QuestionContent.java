package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.io.Serializable;
import java.util.List;


public record QuestionContent(
        String question,
        List<String> options,
        String answer
) implements Serializable {}
package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.util.List;

public record ChatResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {}

    public record Message(
            String role,
            String content
    ) {}
}
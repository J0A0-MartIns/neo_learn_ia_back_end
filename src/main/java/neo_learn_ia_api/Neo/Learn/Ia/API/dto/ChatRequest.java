package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.util.List;

public record ChatRequest(
        String model,
        List<Message> messages
) {
    public record Message(
            String role,
            String content
    ) {}
}

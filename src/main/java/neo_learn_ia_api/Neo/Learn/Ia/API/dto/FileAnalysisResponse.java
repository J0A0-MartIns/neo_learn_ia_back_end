package neo_learn_ia_api.Neo.Learn.Ia.API.dto;


import java.util.List;

public record FileAnalysisResponse(
        List<String> topics,
        String summary
) {}
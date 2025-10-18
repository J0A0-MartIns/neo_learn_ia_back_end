package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.AnalizeDocumentWithAI;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi.OpenAIService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AnalizeDocumentWithAIImpl implements AnalizeDocumentWithAI {

    private final OpenAIService openAIService;

    public Mono<String> generateMultipleChoiceQuestions(MultipartFile file) throws IOException {
        String prompt = "Baseado no arquivo no file_id enviado, gere 5 questões de múltipla escolha com 4 alternativas cada e forneça a resposta correta. "
                + "Responda apenas em JSON no seguinte formato: "
                + "[{\"question\": \"...\", \"options\": [\"A\", \"B\", \"C\", \"D\"], \"answer\": \"A\"}]\n\n";

        return this.openAIService.getChatCompletionWithFile(file,prompt);
    }

}

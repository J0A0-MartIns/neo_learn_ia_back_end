package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.MultipleChoiceQuestionRepository;
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
    private final MultipleChoiceQuestionRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(AnalizeDocumentWithAIImpl.class);

    public Mono<List<MultipleChoiceQuestionEntity>> generateMultipleChoiceQuestions(MultipartFile file) throws IOException {
        String prompt = "Baseado no arquivo no file_id enviado, gere 5 questões de múltipla escolha com 4 alternativas cada e forneça a resposta correta. "
                + "Responda apenas em JSON no seguinte formato: "
                + "[{\"question\": \"...\", \"options\": [\"A\", \"B\", \"C\", \"D\"], \"answer\": \"A\"}]\n\n";


        return openAIService.getChatCompletionWithFile(file, prompt)
                .flatMap(jsonResponse -> {
                    try {
                        List<MultipleChoiceQuestionEntity> questions = objectMapper.readValue(
                                jsonResponse,
                                new com.fasterxml.jackson.core.type.TypeReference<List<MultipleChoiceQuestionEntity>>() {}
                        );

                        logger.debug("Desserialização concluída. {} questões detectadas.", questions.size());
                        for (int i = 0; i < questions.size(); i++) {
                            MultipleChoiceQuestionEntity q = questions.get(i);
                        }

                        List<MultipleChoiceQuestionEntity> saved = repository.saveAll(questions);
                        logger.debug("{} questões salvas no banco com sucesso.", saved.size());

                        return Mono.just(saved);
                    } catch (Exception e) {
                        logger.error("Erro ao processar e salvar questões", e);
                        return Mono.error(new RuntimeException("Erro ao processar e salvar questões: " + e.getMessage(), e));
                    }
                });
    }

    public Mono<List<MultipleChoiceQuizResponse>> getAllQuestions() {
        return Mono.fromSupplier(() ->
                repository.findAll().stream()
                        .map(MultipleChoiceQuizResponse::fromEntity)
                        .toList()
        );
    }
}



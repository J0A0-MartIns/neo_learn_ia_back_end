package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import lombok.AllArgsConstructor;
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

    @Override
    public <T> Mono<T> analyzeFilesAndReturnStudyTopics(
            List<MultipartFile> files,
            JsonResponseFormat format,
            Class<T> responseType
    ) throws IOException {

        if (files == null || files.isEmpty()) {
            return Mono.empty();
        }

        String promptIa = "Analise os arquivos enviados e retorne os principais tópicos de estudo em formato JSON.";

        JsonResponseFormat usedFormat = (format == JsonResponseFormat.STUDY_TOPICS)
                ? JsonResponseFormat.JSON_SCHEMA
                : format;

        return openAIService.analyzeFiles(files, promptIa, usedFormat, responseType);
    }
}

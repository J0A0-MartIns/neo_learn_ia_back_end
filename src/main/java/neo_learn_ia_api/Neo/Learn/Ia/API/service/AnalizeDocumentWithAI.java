package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface AnalizeDocumentWithAI {
    <T> Mono<T> analyzeFilesAndReturnStudyTopics(
            List<MultipartFile> files,
            JsonResponseFormat format,
            Class<T> responseType
    ) throws IOException;
}

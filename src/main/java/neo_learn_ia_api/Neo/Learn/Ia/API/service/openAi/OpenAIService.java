package neo_learn_ia_api.Neo.Learn.Ia.API.service.openAi;

import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface OpenAIService {
    Mono<String> getChatCompletion(String prompt);

     Mono<String> getChatCompletionWithFile(MultipartFile file, String prompt) throws IOException;
}

package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface AnalizeDocumentWithAI {

    Mono<List<MultipleChoiceQuestionEntity>> generateMultipleChoiceQuestions(MultipartFile file) throws IOException;
    Mono<List<MultipleChoiceQuizResponse>> getAllQuestions();
    Mono<String> createScheduleWithFile(ScheduleRequest request) throws IOException;
}

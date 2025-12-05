package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface AnalizeDocumentWithAI {

     Mono<List<MultipleChoiceQuestionEntity>> generateMultipleChoiceQuestions(MultipleChoiceQuizRequest request);
    Mono<List<MultipleChoiceQuizResponse>> getAllQuestions();
    Mono<Long> createScheduleWithFile(ScheduleRequest request) throws IOException;
}

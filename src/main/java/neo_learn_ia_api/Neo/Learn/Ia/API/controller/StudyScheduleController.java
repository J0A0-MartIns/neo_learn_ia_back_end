package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.MultipleChoiceQuizResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ScheduleRequest;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyTopicsResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl.GenericController;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.AnalizeDocumentWithAI;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.AbstractController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("study-schedule")
@AllArgsConstructor
public class StudyScheduleController {

    private final AnalizeDocumentWithAI analyzeFiles;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/generate-questions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> generateQuestions(@RequestPart("file") MultipartFile file) {
        try {
            return analyzeFiles.generateMultipleChoiceQuestions(file)
                    .then(Mono.just("Questões geradas e salvas com sucesso!"));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e));
        }
    }

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MultipleChoiceQuizResponse>> getQuestions() {
        return analyzeFiles.getAllQuestions();
    }

    @PostMapping(value  = "/generate-schedule")
    public Mono<String> generateSchedule(@RequestBody ScheduleRequest request) throws IOException {
        try {
            return analyzeFiles.createScheduleWithFile(request)
                    .then(Mono.just("Cronograma gerado com sucesso"));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e));
        }
    }

}

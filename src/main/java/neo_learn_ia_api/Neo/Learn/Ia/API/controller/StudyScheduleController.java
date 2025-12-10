package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.*;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.MultipleChoiceQuestionEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudySchedule;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.AnalizeDocumentWithAI;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyScheduleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("study-schedule")
@AllArgsConstructor
public class StudyScheduleController {

    private final AnalizeDocumentWithAI analyzeFiles;
    private final ObjectMapper objectMapper;
    private final StudyScheduleService studyScheduleService;

    @PostMapping(value = "/generate-questions")
    public Mono<List<QuestionContent>> generateQuestions(@RequestBody MultipleChoiceQuizRequest request) {
        try {
            return analyzeFiles.generateMultipleChoiceQuestions(request);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e));
        }
    }

    @GetMapping(value = "/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MultipleChoiceQuizResponse>> getQuestions() {
        return analyzeFiles.getAllQuestions();
    }

    @PostMapping(value  = "/generate-schedule")
    public Mono<Long> generateSchedule(@RequestBody ScheduleRequest request) throws IOException {
        try {
            return analyzeFiles.createScheduleWithFile(request);

        } catch (Exception e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyScheduleResponseDTO> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(studyScheduleService.getScheduleById(id));
    }

    @GetMapping
    public List<ScheduleGetResponse> getAll(){
        return studyScheduleService.getAllSchedule();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
            studyScheduleService.deleteScheduleById(id);
            return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generatePdf(@RequestBody QuizPdfRequestDto request) {

        byte[] pdfBytes = studyScheduleService.generatePdf(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=quiz.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}

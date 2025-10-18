package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import lombok.AllArgsConstructor;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyTopicsResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.enums.JsonResponseFormat;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.AnalizeDocumentWithAI;
import org.springframework.http.MediaType;
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

    @PostMapping(
            value = "/analyze-study-topics",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<StudyTopicsResponse> analyzeStudyTopics(
            @RequestPart("files") List<MultipartFile> files
    ) throws IOException {
        return this.analyzeFiles.analyzeFilesAndReturnStudyTopics(
                files,
                JsonResponseFormat.STUDY_TOPICS,
                StudyTopicsResponse.class
        );
    }
}

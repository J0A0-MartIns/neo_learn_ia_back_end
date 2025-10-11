package neo_learn_ia_api.Neo.Learn.Ia.API.controller;


import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/study-project")
public class StudyProjectController {

    private final StudyProjectService studyProjectService;

    public StudyProjectController(StudyProjectService studyProjectService) {
        this.studyProjectService = studyProjectService;
    }

    @PostMapping
    public ResponseEntity<Void> createStudyProject(@ModelAttribute CreateStudyProjectDto dto) {
        try {
            this.studyProjectService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (IOException e) {
            System.err.println("Ocorreu um erro  ao salvar o projeto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<StudyProjectResponseDto>> getAllStudyProjects() {
        List<StudyProjectResponseDto> studyProjects = this.studyProjectService.getAllStudyProject();
        return ResponseEntity.ok(studyProjects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudyProject(@PathVariable UUID id,CreateStudyProjectDto dto){
        try{
            this.studyProjectService.updateStudyProject(id, dto);
            return ResponseEntity.ok("O projeto com ID " + id + " foi atualizado com sucesso.");
        } catch (IOException e){
            System.err.println("Ocorreu um erro  ao atualizar o projeto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyProject(@PathVariable UUID id){
        this.studyProjectService.deleteStudyProjectById(id);
        return ResponseEntity.ok("O projeto com ID " + id + " foi excluído com sucesso.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyProjectResponseDto> getStudyProjectById(@PathVariable UUID id){
        StudyProjectResponseDto response =  this.studyProjectService.getStudyProjectById(id);
        return  ResponseEntity.ok(response);
    }

}

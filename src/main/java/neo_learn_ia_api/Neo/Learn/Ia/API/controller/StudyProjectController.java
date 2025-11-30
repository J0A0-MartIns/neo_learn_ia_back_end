package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl.GenericController;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study-project")
public class StudyProjectController extends GenericController<
        Long,
        CreateStudyProjectDto,
        StudyProjectResponseDto
        > {


    public StudyProjectController(StudyProjectService studyProjectService) {
        super(studyProjectService);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudyProjectResponseDto> create(
            @ModelAttribute CreateStudyProjectDto inputDTO,
            @AuthenticationPrincipal Jwt jwt) {

        Long ownerId = Long.parseLong(jwt.getSubject());

        StudyProjectService projectService = (StudyProjectService) this.service;
        StudyProjectResponseDto responseDTO = projectService.create(inputDTO, ownerId);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<StudyProjectResponseDto> update(@PathVariable Long id, @ModelAttribute CreateStudyProjectDto inputDTO) {
        StudyProjectResponseDto responseDTO = service.update(id, inputDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}/files/{fileId}")
    public ResponseEntity<Void> deleteFileFromProject(
            @PathVariable Long id,
            @PathVariable Long fileId) {


        StudyProjectService projectService = (StudyProjectService) this.service;
        projectService.deleteFileFromProject(id, fileId);

        return ResponseEntity.noContent().build();
    }
}
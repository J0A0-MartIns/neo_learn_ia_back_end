package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.ProjectsForSheduleResponse;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl.GenericController;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return new ResponseEntity<>(
                projectService.create(inputDTO, ownerId),
                HttpStatus.CREATED
        );
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

    @PostMapping("/{id}/publish")
    public ResponseEntity<StudyProjectResponseDto> publish(@PathVariable Long id) {

        StudyProjectService service = (StudyProjectService) this.service;

        return ResponseEntity.ok(service.publish(id));
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<StudyProjectResponseDto> unpublish(@PathVariable Long id) {

        StudyProjectService service = (StudyProjectService) this.service;

        return ResponseEntity.ok(service.unpublish(id));
    }

    @GetMapping("/public-library")
    public ResponseEntity<List<StudyProjectResponseDto>> publicLibrary(@AuthenticationPrincipal Jwt jwt) {

        Long currentUserId = Long.parseLong(jwt.getSubject());

        StudyProjectService projectService = (StudyProjectService) this.service;

        return ResponseEntity.ok(projectService.findPublicLibrary(currentUserId));
    }

    @PostMapping("/{id}/duplicate")
    public ResponseEntity<StudyProjectResponseDto> duplicate(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        Long newOwnerId = Long.parseLong(jwt.getSubject());

        StudyProjectService projectService = (StudyProjectService) this.service;
        StudyProjectResponseDto response = projectService.duplicate(id, newOwnerId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<StudyProjectResponseDto>> findMyProjects(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.parseLong(jwt.getSubject());

        StudyProjectService service = (StudyProjectService) this.service;

        return ResponseEntity.ok(service.findByOwner(userId));
    }

    @GetMapping("/projects-for-shedule")
    public ResponseEntity<List<ProjectsForSheduleResponse>> getProjectsForShedule(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        StudyProjectService projectService = (StudyProjectService) this.service;
        List<ProjectsForSheduleResponse> response = projectService.getProjectsForShedule(userId);
        return ResponseEntity.ok(response);
    }

}
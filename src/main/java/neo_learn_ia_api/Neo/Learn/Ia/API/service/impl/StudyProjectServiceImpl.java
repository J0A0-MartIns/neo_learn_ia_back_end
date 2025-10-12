package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import jakarta.transaction.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.FileMetadataDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyProjectRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.helpers.Helpers;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StudyProjectServiceImpl  implements StudyProjectService {

    private final StudyProjectRepository studyProjectRepository;
    private final FileService fileService;
    private final Helpers helpers;

    public StudyProjectServiceImpl(StudyProjectRepository studyProjectRepository, FileService fileService, Helpers helpers) {
        this.studyProjectRepository = studyProjectRepository;
        this.fileService = fileService;
        this.helpers = helpers;
    }

    @Override()
    public StudyProject save (CreateStudyProjectDto dto) throws IOException {
        this.helpers.validationName(dto.name());

        StudyProject studyProject = new StudyProject();

        helpers.copyFields(dto, studyProject);

        StudyProject savestudyProject = saveFile(dto.file(), studyProject);

        return studyProjectRepository.save(savestudyProject);
    }

    private StudyProject saveFile(List<MultipartFile> file, StudyProject studyProject) throws IOException {
        if (file != null && !file.isEmpty()) {
            for (MultipartFile files : file) {
                if(!file.isEmpty()){
                    FileEntity savedFileEntity = this.fileService.storeFile(files, "Study Project");
                    studyProject.getAttachments().add(savedFileEntity);
                }
            }
        }
        return studyProject;
    }

    @Override
    public List<StudyProjectResponseDto> getAllStudyProject() {
        List<StudyProject> projects = studyProjectRepository.findAll();

        return projects.stream()
                .map(this::convertToDto)
                .toList();
    }

    private StudyProjectResponseDto convertToDto(StudyProject project) {
        List<FileMetadataDto> attachmentDtos = project.getAttachments().stream()
                .map(file -> new FileMetadataDto(
                        file.getId(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getOrigin()))
                .toList();

        return new StudyProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getDescription(),
                attachmentDtos
        );
    }

    @Override
    public StudyProjectResponseDto getStudyProjectById(UUID id) {
        StudyProject entity = studyProjectRepository.findById(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return convertToDto(entity);
    }

    @Override
    public void deleteStudyProjectById(UUID id) {
        studyProjectRepository.deleteById(id);
    }
    @Override
    public void updateStudyProject(UUID id, CreateStudyProjectDto dto)  throws IOException{
        StudyProject studyProject = studyProjectRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Projeto não encontrado com o ID: " + id));
        studyProject.setDescription(dto.description());
        studyProject.setName(dto.name());
        StudyProject savestudyProject =saveFile(dto.file(), studyProject);
        this.studyProjectRepository.save(savestudyProject);
    }

    @Override
    public void deleteFileFromProject(UUID id, Long fileId) {
        StudyProject studyProject = studyProjectRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Projeto não encontrado com o ID: " + id));

        FileEntity fileEntityToDelete = studyProject.getAttachments().stream()
                .filter(file -> file.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Arquivo com ID " + fileId + " não encontrado neste projeto."));

        studyProject.getAttachments().remove(fileEntityToDelete);

        studyProjectRepository.save(studyProject);
    }

}

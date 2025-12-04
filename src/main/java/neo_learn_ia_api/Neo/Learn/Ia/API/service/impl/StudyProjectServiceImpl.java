package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

import jakarta.transaction.Transactional;

import neo_learn_ia_api.Neo.Learn.Ia.API.Exceptions.FileStorageException;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl.AbstractGenericService;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.StudyProjectMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyProjectRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class StudyProjectServiceImpl extends AbstractGenericService<
        StudyProject,
        Long,
        CreateStudyProjectDto,
        StudyProjectResponseDto,
        StudyProjectRepository
        > implements StudyProjectService {

    private final FileService fileService;
    private final StudyProjectMapper mapper;

    public StudyProjectServiceImpl(StudyProjectRepository repository,
                                   FileService fileService,
                                   StudyProjectMapper mapper) {
        super(repository);
        this.fileService = fileService;
        this.mapper = mapper;
    }


    @Override
    protected StudyProject toEntity(CreateStudyProjectDto inputDTO) {
        return mapper.toEntity(inputDTO);
    }

    @Override
    protected StudyProjectResponseDto toResponseDTO(StudyProject entity) {
        return mapper.toResponseDTO(entity);
    }

    @Override
    protected void updateEntityFromDTO(StudyProject entity, CreateStudyProjectDto inputDTO) {
        mapper.updateEntityFromDTO(entity, inputDTO);
    }


    @Override
    public StudyProjectResponseDto create(CreateStudyProjectDto dto) {
        validateProject(dto);

        StudyProject studyProject = toEntity(dto);

        try {
            attachFilesToProject(dto.file(), studyProject);
        } catch (IOException e) {
            throw new FileStorageException("Falha ao processar arquivos para o projeto.", e);
        }

        StudyProject savedProject = repository.save(studyProject);

        return toResponseDTO(savedProject);
    }

    @Override
    public StudyProjectResponseDto update(Long id, CreateStudyProjectDto dto) {
        validateProject(dto);

        StudyProject studyProject = findEntityById(id);

        updateEntityFromDTO(studyProject, dto);

        try {
            attachFilesToProject(dto.file(), studyProject);
        } catch (IOException e) {
            throw new FileStorageException("Falha ao processar novos arquivos para o projeto.", e);
        }

        StudyProject updatedProject = repository.save(studyProject);

        return toResponseDTO(updatedProject);
    }

    @Override
    public void deleteFileFromProject(Long id, Long fileId) {
        StudyProject studyProject = findEntityById(id);

        FileEntity fileEntityToDelete = studyProject.getAttachments().stream()
                .filter(file -> file.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Arquivo com ID " + fileId + " não encontrado neste projeto."));

        studyProject.getAttachments().remove(fileEntityToDelete);
        repository.save(studyProject);
    }



    private void validateProject(CreateStudyProjectDto dto) {
        if (dto.name() == null || dto.name().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do projeto não pode ser nulo ou vazio.");
        }
    }

    private void attachFilesToProject(List<MultipartFile> files, StudyProject studyProject) throws IOException {
        if (files == null || files.isEmpty()) {
            return;
        }

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                FileEntity newFile = this.fileService.buildFileEntity(file, "Study Project");

                studyProject.getAttachments().add(newFile);

            }
        }
    }
}
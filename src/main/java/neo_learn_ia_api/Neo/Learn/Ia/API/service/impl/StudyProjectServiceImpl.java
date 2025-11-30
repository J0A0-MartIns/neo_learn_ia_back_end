package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;

//import jakarta.transaction.Transactional;

import neo_learn_ia_api.Neo.Learn.Ia.API.Exceptions.FileStorageException;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.CreateStudyProjectDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.dto.StudyProjectResponseDto;
import neo_learn_ia_api.Neo.Learn.Ia.API.genericCrud.impl.AbstractGenericService;
import neo_learn_ia_api.Neo.Learn.Ia.API.mapper.StudyProjectMapper;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.StudyProject;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.StudyProjectRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.UserRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.StudyProjectService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

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
    private final UserRepository userRepository;

    public StudyProjectServiceImpl(StudyProjectRepository repository,
                                   FileService fileService,
                                   @Qualifier("studyProjectMapperImpl")StudyProjectMapper mapper,
                                   UserRepository userRepository
                                   ) {
        super(repository);
        this.fileService = fileService;
        this.mapper = mapper;
        this.userRepository = userRepository;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new RuntimeException("Usuário não autenticado ao criar projeto");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long ownerId;
        try {
            ownerId = Long.parseLong(jwt.getSubject());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Subject do JWT inválido para ownerId", ex);
        }

        return create(dto, ownerId);
    }

    @Override
    public StudyProjectResponseDto create(CreateStudyProjectDto dto, Long ownerId) {
        validateProject(dto);

        StudyProject studyProject = toEntity(dto);

        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with id " + ownerId));
        studyProject.setOwner(owner);

        studyProject.setPublic(false);

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

    @Override
    @Transactional(readOnly = true)
    public List<StudyProjectResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StudyProjectResponseDto publish(Long id) {
        StudyProject project = findEntityById(id);
        project.setPublic(true);
        return toResponseDTO(repository.save(project));
    }

    public StudyProjectResponseDto unpublish(Long id) {
        StudyProject project = findEntityById(id);
        project.setPublic(false);
        return toResponseDTO(repository.save(project));
    }

    @Override
    public List<StudyProjectResponseDto> findPublicLibrary(Long currentUserId) {
        return repository.findByOwnerIdNotAndIsPublicTrue(currentUserId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
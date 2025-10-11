package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import jakarta.transaction.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.FileRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileEntity storeFile(MultipartFile file, String origin) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        FileEntity fileEntity = new FileEntity(
                fileName,
                file.getContentType(),
                origin,
                file.getBytes()
        );

        return fileRepository.save(fileEntity);
    }

    @Override
    @Transactional()
    public FileEntity getFileById(UUID id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));
    }


    @Override
    @Transactional()
    public void deleteFile(UUID id) {
        fileRepository.deleteById(id);
    }

    @Override
    public FileEntity updateFile(UUID fileId, MultipartFile file) throws IOException {
        FileEntity existingFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado para atualização: " + fileId));

        existingFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        existingFile.setFileType(file.getContentType());
        existingFile.setData(file.getBytes());

        return fileRepository.save(existingFile);
    }


}

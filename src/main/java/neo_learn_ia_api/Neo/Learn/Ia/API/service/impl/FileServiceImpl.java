package neo_learn_ia_api.Neo.Learn.Ia.API.service.impl;


import org.springframework.transaction.annotation.Transactional;
import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.repository.FileRepository;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileEntity storeFile(MultipartFile file, String origin) throws IOException {
        FileEntity fileEntity = buildFileEntity(file, origin);
        return fileRepository.save(fileEntity);
    }

    @Override
    public FileEntity buildFileEntity(MultipartFile file, String origin) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IOException("Falha ao processar arquivo vazio: " + fileName);
        }

        return new FileEntity(
                fileName,
                file.getContentType(),
                origin,
                file.getBytes()
        );
    }

    @Override
    @Transactional()
    public FileEntity getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado com o ID: " + id));
    }

    @Override
    @Transactional()
    public void deleteFile(Long id) {
        fileRepository.deleteById(id);
    }

    @Override
    public FileEntity updateFile(Long fileId, MultipartFile file) throws IOException {
        FileEntity existingFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado para atualização: " + fileId));

        existingFile.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
        existingFile.setFileType(file.getContentType());
        existingFile.setData(file.getBytes());

        return fileRepository.save(existingFile);
    }

    @Override
    public List<FileEntity> findByStudyProjectId(Long projectId) {
        return fileRepository.findByStudyProjectId(projectId);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateZipForProject(Long projectId) {

        List<FileEntity> files = fileRepository.findByStudyProjectId(projectId);

        if (files.isEmpty()) return new byte[0];

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (FileEntity file : files) {
                ZipEntry entry = new ZipEntry(file.getFileName());
                zos.putNextEntry(entry);
                zos.write(file.getData());
                zos.closeEntry();
            }

            zos.finish();
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar ZIP", e);
        }
    }
}

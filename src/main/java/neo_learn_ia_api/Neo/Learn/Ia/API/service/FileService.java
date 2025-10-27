package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {

    FileEntity storeFile(MultipartFile file, String origin)throws IOException;
    FileEntity getFileById(Long id);
    void deleteFile(Long id);
    FileEntity updateFile(Long fileId, MultipartFile file) throws IOException;
    FileEntity buildFileEntity(MultipartFile file, String origin) throws IOException;
}

package neo_learn_ia_api.Neo.Learn.Ia.API.service;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface FileService {

    FileEntity storeFile(MultipartFile file, String origin)throws IOException;
    FileEntity getFileById(UUID id);
    void deleteFile(UUID id);
    FileEntity updateFile(UUID fileId, MultipartFile file) throws IOException;
}

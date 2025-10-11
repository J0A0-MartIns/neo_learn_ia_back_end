package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable UUID id) {
        FileEntity fileEntity = fileService.getFileById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getFileType()));
        headers.setContentDispositionFormData("attachment", fileEntity.getFileName());
        return new ResponseEntity<>(fileEntity.getData(), headers, 200);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable UUID id) {
        this.fileService.deleteFile(id);
        return ResponseEntity.ok("Arquivo Deletado com sucesso.");
    }
}

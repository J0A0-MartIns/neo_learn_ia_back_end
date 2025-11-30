package neo_learn_ia_api.Neo.Learn.Ia.API.controller;

import neo_learn_ia_api.Neo.Learn.Ia.API.model.FileEntity;
import neo_learn_ia_api.Neo.Learn.Ia.API.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileEntity fileEntity = fileService.getFileById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getFileType()));
        headers.setContentDispositionFormData("attachment", fileEntity.getFileName());
        return new ResponseEntity<>(fileEntity.getData(), headers, 200);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        FileEntity fileEntity = fileService.getFileById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileEntity.getFileType()));

        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + fileEntity.getFileName() + "\"");

        return new ResponseEntity<>(fileEntity.getData(), headers, 200);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        this.fileService.deleteFile(id);
        return ResponseEntity.ok("Arquivo Deletado com sucesso.");
    }

    @GetMapping("/download-all/{projectId}")
    public ResponseEntity<byte[]> downloadAll(@PathVariable Long projectId) {

        byte[] zip = fileService.generateZipForProject(projectId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"projeto_" + projectId + ".zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zip);
    }
}

package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateStudyProjectDto(List<MultipartFile> file, String name, String description) {
}

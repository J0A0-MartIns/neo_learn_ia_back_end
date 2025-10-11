package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateStudyProjectDto(MultipartFile file, String name, String description) {
}

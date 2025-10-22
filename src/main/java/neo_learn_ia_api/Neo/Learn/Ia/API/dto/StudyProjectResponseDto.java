package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.util.List;
import java.util.UUID;

public record StudyProjectResponseDto(
        Long id,
        String name,
        String description,
        List<FileMetadataDto> attachments
) {}
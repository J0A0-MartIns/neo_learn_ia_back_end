package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.time.LocalDateTime;
import java.util.List;

public record StudyProjectResponseDto(
        Long id,
        String name,
        String description,
        List<FileMetadataDto> attachments,
        String ownerName,
        Long ownerId,
        Long originalProjectId,
        LocalDateTime createdAt,
        Boolean isPublic
) {}
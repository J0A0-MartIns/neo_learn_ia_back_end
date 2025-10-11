package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

import java.util.UUID;

public record FileMetadataDto(
        UUID id,
        String fileName,
        String fileType,
        String origin
) {}
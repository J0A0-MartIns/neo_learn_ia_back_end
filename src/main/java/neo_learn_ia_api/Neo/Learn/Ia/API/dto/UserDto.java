package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

public record UserDto(
        Long id,
        String userEmail,
        String userFirstName,
        String telefone,
        String cargo,
        String instituicao
) {
}

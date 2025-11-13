package neo_learn_ia_api.Neo.Learn.Ia.API.dto;

public record UpdateUserDto(
        String userEmail,
        String userFirstName,
        String telefone,
        String cargo,
        String instituicao
) {
}

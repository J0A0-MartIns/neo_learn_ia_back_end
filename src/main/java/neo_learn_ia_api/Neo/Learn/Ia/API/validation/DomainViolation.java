package neo_learn_ia_api.Neo.Learn.Ia.API.validation;


import lombok.Value;

@Value
public class DomainViolation {
    String field;
    String message;
    String code;
}
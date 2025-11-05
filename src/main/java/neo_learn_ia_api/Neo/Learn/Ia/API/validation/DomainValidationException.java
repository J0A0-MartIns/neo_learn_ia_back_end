package neo_learn_ia_api.Neo.Learn.Ia.API.validation;

import java.util.List;

public class DomainValidationException extends RuntimeException {
    private final List<DomainViolation> violations;

    public DomainValidationException(List<DomainViolation> violations) {
        super("Domain validation failed");
        this.violations = violations;
    }

    public List<DomainViolation> getViolations() {
        return violations;
    }
}

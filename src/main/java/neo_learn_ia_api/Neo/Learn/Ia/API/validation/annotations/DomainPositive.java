package neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Aceita > 0 (para numéricos). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DomainPositive {
    String message() default "deve ser positivo";
}

package neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Validação simples de e-mail (formato). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DomainEmail {
    String message() default "e-mail inválido";
    /** Regex opcional (caso queira customizar). */
    String pattern() default "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
}

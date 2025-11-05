package neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
public @interface DomainSize {
    int min() default 0;
    int max() default Integer.MAX_VALUE;
    String message() default "tamanho inválido";
}

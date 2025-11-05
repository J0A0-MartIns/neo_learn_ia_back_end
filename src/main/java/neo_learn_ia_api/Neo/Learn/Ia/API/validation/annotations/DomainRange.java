package neo_learn_ia_api.Neo.Learn.Ia.API.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Para valores numéricos (Integer, Long, BigDecimal, etc.). */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DomainRange {
    String message() default "fora do intervalo permitido";
    long min() default Long.MIN_VALUE; // inclusive
    long max() default Long.MAX_VALUE; // inclusive
}

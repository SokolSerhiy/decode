package ua.zlata.decode.argument.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Operations {

    String name() default "operation";
}

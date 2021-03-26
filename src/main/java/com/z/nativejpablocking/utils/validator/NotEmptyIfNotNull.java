package com.z.nativejpablocking.utils.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = NotEmptyIfNotNullValidator.class)
@Documented
public @interface NotEmptyIfNotNull {
    Class<?>[] groups() default { };

    String message();

    Class<? extends Payload>[] payload() default { };
}

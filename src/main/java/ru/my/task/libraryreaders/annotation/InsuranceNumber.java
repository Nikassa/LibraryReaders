package ru.my.task.libraryreaders.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InsuranceNumberConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InsuranceNumber {

    String message() default "Incorrect InsuranceNumber";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
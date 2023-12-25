package com.ead.authuser.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameConstraintImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {

    String message() default "Nome de usuário inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

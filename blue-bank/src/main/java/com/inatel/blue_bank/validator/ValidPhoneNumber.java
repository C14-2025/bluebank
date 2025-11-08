package com.inatel.blue_bank.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "{ValidPhoneNumber.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


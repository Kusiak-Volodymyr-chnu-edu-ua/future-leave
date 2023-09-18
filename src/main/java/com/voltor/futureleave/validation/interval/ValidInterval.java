package com.voltor.futureleave.validation.interval;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntervalValidator.class)
public @interface ValidInterval {

	String message() default "from value must not be till value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}

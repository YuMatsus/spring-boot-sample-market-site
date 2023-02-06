package com.example.market.validators
;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistsUserEmailValidator.class})
public @interface ExistsUserEmail {

    String message() default "該当のメールアドレスは登録済みです。";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.example.market.validators
;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.market.services.UserService;

public class ExistsUserEmailValidator implements ConstraintValidator<ExistsUserEmail, String>{

    @Autowired
    private UserService userService;
    
    @Override
    public void initialize(ExistsUserEmail existsUserEmail) {        
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userService.existsByEmail(value);
    }

}

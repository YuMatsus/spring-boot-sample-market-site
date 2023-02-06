package com.example.market.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.market.repositories.CategoryRepository;

public class ExistsCategoryIdValidator implements ConstraintValidator<ExistsCategoryId, Long>{

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Override
    public void initialize(ExistsCategoryId existsCategoryId) {        
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return categoryRepository.existsById(value);
    }

}

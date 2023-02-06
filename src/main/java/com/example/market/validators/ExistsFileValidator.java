package com.example.market.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class ExistsFileValidator implements ConstraintValidator<ExistsFile, MultipartFile>{
    
    @Override
    public void initialize(ExistsFile existsFile) {        
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if(file.getSize() > 0){
            return true;
        }
        return false;
    }

}

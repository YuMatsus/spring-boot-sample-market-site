package com.example.market.validators;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class CheckFileExtensionValidator implements ConstraintValidator<CheckFileExtension, MultipartFile>{

    private String[] extensions;

    @Override
    public void initialize(CheckFileExtension checkFileExtension) {    
        extensions = checkFileExtension.extensions();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(extensions).contains(extension);
    }

}

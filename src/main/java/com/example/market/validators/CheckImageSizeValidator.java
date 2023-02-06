package com.example.market.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class CheckImageSizeValidator implements ConstraintValidator<CheckImageSize, MultipartFile>{

    private int min;
    private int max;

    @Override
    public void initialize(CheckImageSize CheckImageSize) {    
        min = CheckImageSize.min();
        max = CheckImageSize.max();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image != null) {
                if(
                    image.getWidth() < min ||
                    image.getHeight() < min ||
                    image.getWidth() > max ||
                    image.getHeight() > max
                ) {
                    return false;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

}

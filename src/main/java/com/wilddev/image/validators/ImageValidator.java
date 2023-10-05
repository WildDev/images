package com.wilddev.image.validators;

import com.wilddev.image.enums.ImageType;
import com.wilddev.image.testers.ImageContentTypeTester;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.*;

@Component
public class ImageValidator implements Validator {

    private final FileValidator validator;

    public ImageValidator(@Value("${image.allowed.types}") Set<ImageType> imageTypes) {
        this.validator = new FileValidator(new ImageContentTypeTester(imageTypes));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MultipartFile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validator.validate(target, errors);
    }
}

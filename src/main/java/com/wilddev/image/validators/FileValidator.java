package com.wilddev.image.validators;

import com.wilddev.image.testers.ContentTypeTester;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.*;

@AllArgsConstructor
public class FileValidator implements Validator {

    private final ContentTypeTester contentTypeTester;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MultipartFile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (!contentTypeTester.test(((MultipartFile) target).getContentType()))
            errors.rejectValue("file", "content.type.unsupported", "Unsupported content type");
    }
}

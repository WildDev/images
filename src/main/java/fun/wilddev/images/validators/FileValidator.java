package fun.wilddev.images.validators;

import fun.wilddev.images.testers.ContentTypeTester;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
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
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        if (!contentTypeTester.test(((MultipartFile) target).getContentType()))
            errors.rejectValue("file", "content.type.unsupported", "Unsupported content type");
    }
}

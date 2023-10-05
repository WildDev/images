package fun.wilddev.images.validators;

import fun.wilddev.images.controllers.requests.ImageUploadRequest;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import org.springframework.validation.*;

@AllArgsConstructor
@Component
public class ImageUploadRequestValidator implements Validator {

    private final ImageValidator imageValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ImageUploadRequest.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        imageValidator.validate(((ImageUploadRequest) target).getFile(), errors);
    }
}

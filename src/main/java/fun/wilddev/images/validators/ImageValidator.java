package fun.wilddev.images.validators;

import fun.wilddev.images.config.props.ImageProps;
import fun.wilddev.images.testers.ContentTypeTester;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.validation.*;

@Component
public class ImageValidator implements Validator {

    private final FileValidator validator;

    public ImageValidator(ImageProps imageProps) {
        this.validator = new FileValidator(new ContentTypeTester(imageProps.allowedTypes()));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MultipartFile.class);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        validator.validate(target, errors);
    }
}

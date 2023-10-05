package fun.wilddev.images.controllers.requests;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Setter
@Getter
@ToString(callSuper = true)
public class ImageUploadRequest extends ImageFilterRequest {

    @NotNull
    private MultipartFile file;
}

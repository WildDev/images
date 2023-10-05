package fun.wilddev.images.controllers.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import lombok.*;

@Setter
@Getter
@ToString(callSuper = true)
public class ExternalImageAddRequest extends ImageFilterRequest {

    @URL(message = "Not a URL")
    @NotBlank
    private String url;
}

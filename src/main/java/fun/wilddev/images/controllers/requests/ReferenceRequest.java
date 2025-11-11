package fun.wilddev.images.controllers.requests;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Setter
@Getter
@ToString
public class ReferenceRequest {

    @NotBlank
    protected String id;
}

package com.wilddev.image.sdk.controllers.responses.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class FieldErrorResponse {

    @JsonProperty
    private String field;

    @JsonProperty
    private String message;
}

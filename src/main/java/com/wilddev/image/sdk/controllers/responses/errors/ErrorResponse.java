package com.wilddev.image.sdk.controllers.responses.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty
    private List<FieldErrorResponse> errors;
}

package com.wilddev.image.sdk.controllers.responses.errors;

import java.util.List;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ErrorResponse {

    private List<FieldErrorResponse> errors;
}

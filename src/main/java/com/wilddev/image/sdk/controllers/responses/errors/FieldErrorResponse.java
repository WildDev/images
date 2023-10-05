package com.wilddev.image.sdk.controllers.responses.errors;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class FieldErrorResponse {

    private String field;

    private String message;
}

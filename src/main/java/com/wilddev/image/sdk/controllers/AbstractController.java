package com.wilddev.image.sdk.controllers;

import java.util.List;

import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;

import com.wilddev.image.sdk.controllers.responses.errors.*;
import org.springframework.http.*;

public abstract class AbstractController {

    public <T> ResponseEntity<?> created(T body) {
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    public <T> ResponseEntity<?> ok(T body, MultiValueMap<String, String> headers) {
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    public ResponseEntity<?> notFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> badRequest(Errors errors) {

        List<FieldErrorResponse> response = errors.getFieldErrors().stream().map(e ->
                new FieldErrorResponse(e.getField(), e.getDefaultMessage())).toList();

        return new ResponseEntity<>(new ErrorResponse(response), HttpStatus.BAD_REQUEST);
    }
}

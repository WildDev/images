package com.wilddev.image.controllers.requests;

import jakarta.validation.constraints.NotNull;

import lombok.*;

@Setter
@Getter
@ToString
public abstract class ImageFilterRequest {

    @NotNull
    protected Boolean multiSize;
}

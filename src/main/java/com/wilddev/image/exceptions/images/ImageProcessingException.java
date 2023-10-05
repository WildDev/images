package com.wilddev.image.exceptions.images;

public abstract class ImageProcessingException extends ImageException {

    protected ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

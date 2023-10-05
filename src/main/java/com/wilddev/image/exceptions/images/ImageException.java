package com.wilddev.image.exceptions.images;

import com.wilddev.image.exceptions.files.FileException;

public abstract class ImageException extends FileException {

    protected ImageException(String message) {
        super(message);
    }

    protected ImageException(String message, Throwable cause) {
        super(message, cause);
    }
}

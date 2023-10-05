package com.wilddev.image.exceptions.async.image;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.async.CompletionStageException;

public class ImageFailedException extends CompletionStageException {

    public ImageFailedException(String message, Image image, Throwable cause) {
        super(message, image, cause);
    }
}

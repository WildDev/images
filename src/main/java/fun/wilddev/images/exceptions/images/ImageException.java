package fun.wilddev.images.exceptions.images;

import fun.wilddev.images.exceptions.files.FileException;

public abstract class ImageException extends FileException {

    protected ImageException(String message) {
        super(message);
    }

    protected ImageException(String message, Throwable cause) {
        super(message, cause);
    }
}

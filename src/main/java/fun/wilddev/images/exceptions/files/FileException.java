package fun.wilddev.images.exceptions.files;

public abstract class FileException extends Exception {

    protected FileException(String message) {
        super(message);
    }

    protected FileException(String message, Throwable cause) {
        super(message, cause);
    }
}

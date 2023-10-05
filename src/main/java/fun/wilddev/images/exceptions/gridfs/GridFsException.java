package fun.wilddev.images.exceptions.gridfs;

public abstract class GridFsException extends Exception {

    protected GridFsException(String message) {
        super(message);
    }

    protected GridFsException(String message, Throwable cause) {
        super(message, cause);
    }
}

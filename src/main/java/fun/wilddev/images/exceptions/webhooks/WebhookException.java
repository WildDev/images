package fun.wilddev.images.exceptions.webhooks;

public abstract class WebhookException extends Exception {

    protected WebhookException(String message) {
        super(message);
    }

    protected WebhookException(String message, Throwable cause) {
        super(message, cause);
    }
}

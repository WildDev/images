package com.wilddev.image.exceptions.webhooks;

public class WebhookDeliveryException extends WebhookException {

    public WebhookDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}

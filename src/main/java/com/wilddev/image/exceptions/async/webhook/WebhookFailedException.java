package com.wilddev.image.exceptions.async.webhook;

import com.wilddev.image.entities.webhooks.Webhook;

public class WebhookFailedException extends WebhookCompletionStageException {

    public WebhookFailedException(String message, Webhook webhook, Throwable cause) {
        super(message, webhook, cause);
    }
}

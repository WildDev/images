package com.wilddev.image.exceptions.async.webhook;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.exceptions.async.CompletionStageException;

public abstract class WebhookCompletionStageException extends CompletionStageException {

    protected WebhookCompletionStageException(String message, Webhook webhook) {
        super(message, webhook);
    }

    protected WebhookCompletionStageException(String message, Webhook webhook, Throwable cause) {
        super(message, webhook, cause);
    }
}

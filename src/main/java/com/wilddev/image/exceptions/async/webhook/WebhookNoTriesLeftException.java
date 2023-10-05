package com.wilddev.image.exceptions.async.webhook;

import com.wilddev.image.entities.webhooks.Webhook;

public class WebhookNoTriesLeftException extends WebhookCompletionStageException {

    public WebhookNoTriesLeftException(String message, Webhook webhook) {
        super(message, webhook);
    }
}

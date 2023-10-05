package com.wilddev.image.services.webhooks.sender;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.exceptions.webhooks.WebhookException;
import com.wilddev.image.sdk.models.Sender;

public interface WebhookSender extends Sender<Webhook> {

    void send(Webhook webhook) throws WebhookException;
}

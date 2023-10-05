package com.wilddev.image.services.webhooks.sender;

import com.wilddev.image.exceptions.webhooks.WebhookDeliveryException;
import com.wilddev.image.sdk.models.Sender;
import com.wilddev.image.services.webhooks.sender.http.WebhookRequest;

public interface WebhookDeliveryService<T extends WebhookRequest> extends Sender<T> {

    void send(T request) throws WebhookDeliveryException;
}

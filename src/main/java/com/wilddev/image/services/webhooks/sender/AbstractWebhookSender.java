package com.wilddev.image.services.webhooks.sender;

import com.wilddev.image.entities.webhooks.meta.WebhookMeta;
import com.wilddev.image.services.webhooks.WebhookService;
import com.wilddev.image.services.webhooks.meta.WebhookMetadataReader;
import com.wilddev.image.services.webhooks.sender.http.WebhookRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractWebhookSender<T extends WebhookRequest, Y extends WebhookMeta> implements WebhookSender {

    protected final WebhookMetadataReader<Y> webhookMetadataReader;

    protected final WebhookService webhookService;

    protected final WebhookDeliveryService<T> webhookDeliveryService;
}

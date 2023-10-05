package com.wilddev.image.services.webhooks.meta;

import com.wilddev.image.entities.webhooks.meta.WebhookMeta;
import com.wilddev.image.exceptions.webhooks.MalformedWebhookMetadataException;

import com.wilddev.image.entities.webhooks.*;

public interface WebhookMetadataReader<T extends WebhookMeta> {

    T read(Webhook webhook) throws MalformedWebhookMetadataException;
}

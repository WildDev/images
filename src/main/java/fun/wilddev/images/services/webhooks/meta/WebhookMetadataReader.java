package fun.wilddev.images.services.webhooks.meta;

import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.entities.webhooks.meta.WebhookMeta;
import fun.wilddev.images.exceptions.webhooks.MalformedWebhookMetadataException;

public interface WebhookMetadataReader<T extends WebhookMeta> {

    T read(Webhook webhook) throws MalformedWebhookMetadataException;
}

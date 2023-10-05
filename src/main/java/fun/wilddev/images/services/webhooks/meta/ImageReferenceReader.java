package fun.wilddev.images.services.webhooks.meta;

import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.exceptions.webhooks.MalformedWebhookMetadataException;
import fun.wilddev.spring.core.services.MessageService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fun.wilddev.images.entities.webhooks.meta.*;

@AllArgsConstructor
@Component
public class ImageReferenceReader implements WebhookMetadataReader<ImageReference> {

    private final MessageService messageService;

    @Override
    public ImageReference read(@NonNull Webhook webhook) throws MalformedWebhookMetadataException {

        final WebhookMeta webhookMeta = webhook.getMeta();

        if (webhookMeta == null)
            throw new MalformedWebhookMetadataException(messageService
                    .getMessage("exception.webhook.incompatible.metadata", webhook.getId()));

        if (webhookMeta instanceof final ImageReference ref)
            return ref;

        throw new MalformedWebhookMetadataException(messageService
                .getMessage("exception.webhook.metadata.not.found", webhook.getId()));
    }
}

package com.wilddev.image.services.webhooks.meta;

import com.wilddev.image.exceptions.webhooks.MalformedWebhookMetadataException;
import com.wilddev.image.services.MessageService;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.wilddev.image.entities.webhooks.*;
import com.wilddev.image.entities.webhooks.meta.*;

@AllArgsConstructor
@Component
public class ImageReferenceReader implements WebhookMetadataReader<ImageReference> {

    private final MessageService messageService;

    @LogTimeExecuted("ImageReferenceReader#read(webhook)")
    @Override
    public ImageReference read(@NonNull Webhook webhook) throws MalformedWebhookMetadataException {

        final WebhookMeta webhookMeta = webhook.getMeta();

        if (webhookMeta == null)
            throw new MalformedWebhookMetadataException(messageService
                    .getMessage("exception.webhook.incompatible.metadata", webhook.getId()));

        if (webhookMeta instanceof ImageReference)
            return (ImageReference) webhookMeta;

        throw new MalformedWebhookMetadataException(messageService
                .getMessage("exception.webhook.metadata.not.found", webhook.getId()));
    }
}

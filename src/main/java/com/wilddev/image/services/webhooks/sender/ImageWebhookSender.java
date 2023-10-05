package com.wilddev.image.services.webhooks.sender;

import com.wilddev.image.entities.Image;
import com.wilddev.image.entities.webhooks.meta.ImageReference;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.webhooks.WebhookService;
import com.wilddev.image.services.webhooks.meta.ImageReferenceReader;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wilddev.image.entities.webhooks.*;
import com.wilddev.image.exceptions.webhooks.*;
import com.wilddev.image.services.webhooks.sender.http.*;

@Slf4j
@Service
public class ImageWebhookSender extends AbstractWebhookSender<ImageWebhookRequest, ImageReference> {

    public ImageWebhookSender(ImageReferenceReader metadataReader,
                              WebhookService webhookService,
                              WebhookDeliveryService<ImageWebhookRequest> deliveryService) {
        super(metadataReader, webhookService, deliveryService);
    }

    private String getImageId(ImageReference webhookMeta) {
        return Optional.ofNullable(webhookMeta.getImage())
                .map(Image::getId).map(ObjectId::toString).orElse(null);
    }

    @LogTimeExecuted("ImageWebhookSender#send(webhook)")
    @Override
    public void send(@NonNull Webhook webhook) throws WebhookException {

        ImageReference webhookMeta = webhookMetadataReader.read(webhook);
        log.debug("Webhook metadata: {}", webhookMeta);

        webhookDeliveryService.send(new ImageWebhookRequest(webhook.getType(), getImageId(webhookMeta)));
    }
}

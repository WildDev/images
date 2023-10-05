package com.wilddev.image.services.webhooks;

import com.wilddev.image.entities.Image;
import com.wilddev.image.entities.webhooks.meta.ImageReference;
import com.wilddev.image.enums.WebhookType;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Component
public class ImageWebhookService {

    private final WebhookService webhookService;

    @LogTimeExecuted("ImageWebhookService#sendImageReady(image)")
    @Transactional
    public void sendImageReady(@NonNull Image image) {
        webhookService.add(WebhookType.IMAGE_READY, new ImageReference(image));
    }

    @LogTimeExecuted("ImageWebhookService#sendImageFailed(image)")
    @Transactional
    public void sendImageFailed(@NonNull Image image) {
        webhookService.add(WebhookType.IMAGE_FAILED, new ImageReference(image));
    }
}

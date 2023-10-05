package fun.wilddev.images.services.webhooks;

import fun.wilddev.images.entities.webhooks.meta.ImageReference;
import fun.wilddev.images.enums.WebhookType;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Component
public class ImageWebhookService {

    private final WebhookService webhookService;

    @Transactional
    public void sendImageReady(@NonNull String id) {
        webhookService.add(WebhookType.IMAGE_READY, new ImageReference(id));
    }

    @Transactional
    public void sendImageFailed(@NonNull String id) {
        webhookService.add(WebhookType.IMAGE_FAILED, new ImageReference(id));
    }
}

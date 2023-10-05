package fun.wilddev.images.services;

import fun.wilddev.images.services.webhooks.ImageWebhookService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ImageFailureSetter implements FailureSetter {

    private final ImageService imageService;

    private final ImageWebhookService imageWebhookService;

    @Transactional
    @Override
    public void setFailed(@NonNull String id) {

        imageService.setFailed(id);
        imageWebhookService.sendImageFailed(id);
    }
}

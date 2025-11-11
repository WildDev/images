package fun.wilddev.images.handlers;

import fun.wilddev.images.rabbitmq.data.ImageRemovalData;
import fun.wilddev.images.services.webhooks.WebhookService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import fun.wilddev.images.services.*;

@Slf4j
@AllArgsConstructor
@Service
public class ImageRemovalHandler implements Handler<ImageRemovalData> {

    private final GridFsService gridFsService;

    private final ImageService imageService;

    private final WebhookService webhookService;

    @Override
    public void handle(@NonNull ImageRemovalData data) {

        final String id = data.getId();

        gridFsService.delete(id);
        imageService.delete(id);
        webhookService.deleteByImageId(id);
    }
}

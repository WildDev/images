package fun.wilddev.images.processors;

import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.services.webhooks.ImageWebhookService;

import java.awt.image.BufferedImage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fun.wilddev.images.services.*;

@Slf4j
@Component
public class ImageProcessor {

    private final ImageService imageService;

    private final ImageTaskRunner imageTaskRunner;

    private final ImageWebhookService imageWebhookService;

    public ImageProcessor(ImageService imageService,
                          ImageTaskRunner imageTaskRunner,
                          ImageWebhookService imageWebhookService) {

        this.imageService = imageService;
        this.imageTaskRunner = imageTaskRunner;
        this.imageWebhookService = imageWebhookService;
    }

    public void process(@NonNull BufferedImage bufferedImage, @NonNull FileMeta fileMeta) {

        final String id = fileMeta.id();

        imageTaskRunner.runTasks(bufferedImage, fileMeta);

        imageService.setProcessed(id);
        imageWebhookService.sendImageReady(id);
    }
}

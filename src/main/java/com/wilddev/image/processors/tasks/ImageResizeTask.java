package com.wilddev.image.processors.tasks;

import com.wilddev.image.entities.settings.ResizeSize;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.logics.CollectionItemProcessor;
import com.wilddev.image.services.SettingsService;
import com.wilddev.image.services.editors.ImageEditorService;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component("RESIZE")
public class ImageResizeTask extends ImageEditorTask {

    private final CollectionItemProcessor<ResizeSize> collectionProcessor;

    public ImageResizeTask(SettingsService settingsService,
                           ImageEditorService imageEditorService) {

        super(settingsService, imageEditorService);
        this.collectionProcessor = new CollectionItemProcessor<>(log);
    }

    @LogTimeExecuted("ImageResizeTask#run(source)")
    @Async
    @Override
    public CompletableFuture<Void> run(@NonNull SourceImage source) {

        collectionProcessor.process(settingsService::listResizeSizes, size -> {

            try {
                imageEditorService.resizeAndStore(source, size);
            } catch (Exception ex) {
                log.error("Failed to resize image {} to {}", source.image().getId(), size, ex);
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}

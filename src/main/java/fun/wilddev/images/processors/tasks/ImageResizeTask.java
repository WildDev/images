package fun.wilddev.images.processors.tasks;

import fun.wilddev.images.entities.settings.ResizeSize;
import fun.wilddev.images.experimental.CollectionItemProcessor;
import fun.wilddev.images.services.SettingsService;
import fun.wilddev.images.services.editors.ImageEditorService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
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

    @Override
    public void run(@NonNull SourceImage source) {

        collectionProcessor.process(settingsService::listResizeSizes, size -> {

            try {
                imageEditorService.resizeAndStore(source, size);
            } catch (Exception ex) {
                log.error("Failed to resize image {} to {}", source.fileMeta().id(), size, ex);
            }
        });
    }
}

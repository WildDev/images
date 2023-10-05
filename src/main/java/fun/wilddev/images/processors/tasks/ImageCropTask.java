package fun.wilddev.images.processors.tasks;

import fun.wilddev.images.entities.settings.CropSize;
import fun.wilddev.images.experimental.CollectionItemProcessor;
import fun.wilddev.images.services.SettingsService;
import fun.wilddev.images.services.editors.ImageEditorService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component("CROP")
public class ImageCropTask extends ImageEditorTask {

    private final CollectionItemProcessor<CropSize> collectionProcessor;

    public ImageCropTask(SettingsService settingsService,
                         ImageEditorService imageEditorService) {

        super(settingsService, imageEditorService);
        this.collectionProcessor = new CollectionItemProcessor<>(log);
    }

    @Override
    public void run(@NonNull SourceImage source) {

        collectionProcessor.process(settingsService::listCropSizes, size -> {

            try {
                imageEditorService.cropAndStore(source, size);
            } catch (Exception ex) {
                log.error("Failed to crop image {} to size {}", source.fileMeta().id(), size, ex);
            }
        });
    }
}

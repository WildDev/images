package fun.wilddev.images.processors.tasks;

import fun.wilddev.images.services.SettingsService;
import fun.wilddev.images.services.editors.ImageEditorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ImageEditorTask implements ImagePostProcessingTask {

    protected final SettingsService settingsService;

    protected final ImageEditorService imageEditorService;
}

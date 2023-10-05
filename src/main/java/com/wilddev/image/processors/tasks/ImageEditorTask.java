package com.wilddev.image.processors.tasks;

import com.wilddev.image.services.SettingsService;
import com.wilddev.image.services.editors.ImageEditorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class ImageEditorTask implements ImagePostProcessingTask {

    protected final SettingsService settingsService;

    protected final ImageEditorService imageEditorService;
}

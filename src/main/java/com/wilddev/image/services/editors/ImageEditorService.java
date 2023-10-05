package com.wilddev.image.services.editors;

import com.wilddev.image.exceptions.images.ImageException;
import com.wilddev.image.processors.tasks.SourceImage;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.wilddev.image.entities.settings.*;
import com.wilddev.image.services.editors.effects.*;

@Slf4j
@Service
public class ImageEditorService {

    private final ImageCustomSizeEffectProxy<CropSize> cropEditor;

    private final ImageCustomSizeEffectProxy<ResizeSize> resizeEditor;

    public ImageEditorService(ImageCropEffect cropEditor,
                              ImageResizeEffect resizeEditor) {

        this.cropEditor = new ImageCustomSizeEffectProxy<>(log, cropEditor);
        this.resizeEditor = new ImageCustomSizeEffectProxy<>(log, resizeEditor);
    }

    @LogTimeExecuted("ImageEditorService#cropAndStore(sourceImage, target)")
    public void cropAndStore(@NonNull SourceImage sourceImage, @NonNull CropSize target) throws ImageException {
        cropEditor.accept(sourceImage, target);
    }

    @LogTimeExecuted("ImageEditorService#resizeAndStore(sourceImage, target)")
    public void resizeAndStore(@NonNull SourceImage sourceImage, @NonNull ResizeSize target) throws ImageException {
        resizeEditor.accept(sourceImage, target);
    }
}

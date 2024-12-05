package fun.wilddev.images.services.editors;

import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.processors.tasks.SourceImage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import fun.wilddev.images.entities.settings.*;
import fun.wilddev.images.services.editors.effects.*;

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

    public void cropAndStore(@NonNull SourceImage sourceImage, @NonNull CropSize target) throws FileException {
        cropEditor.accept(sourceImage, target);
    }

    public void resizeAndStore(@NonNull SourceImage sourceImage, @NonNull ResizeSize target) throws FileException {
        resizeEditor.accept(sourceImage, target);
    }
}

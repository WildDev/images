package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.entities.settings.AbstractSize;
import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.processors.tasks.SourceImage;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;

import java.awt.*;

@AllArgsConstructor
public class ImageCustomSizeEffectProxy<T extends AbstractSize>
        implements FileExceptionThrowingConsumer<SourceImage, T> {

    private final Logger log;

    private final ImageCustomSizeEffect<T> imageCustomSizeEffect;

    @Override
    public void accept(@NonNull SourceImage sourceImage, @NonNull T targetSize) throws FileException {

        final Dimension targetDimension = imageCustomSizeEffect
                .calcTargetDimension(sourceImage.bufferedImage(), targetSize);

        if (targetDimension.equals(sourceImage.dimension())) {

            log.debug("Dimensions are equal");
            return;
        }

        imageCustomSizeEffect.accept(sourceImage, targetDimension);
    }
}

package com.wilddev.image.services.editors.effects;

import com.wilddev.image.entities.settings.AbstractSize;
import com.wilddev.image.exceptions.images.ImageException;
import com.wilddev.image.models.ImageExceptionThrowingConsumer;
import com.wilddev.image.processors.tasks.SourceImage;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.springframework.lang.NonNull;

import java.awt.*;

@AllArgsConstructor
public class ImageCustomSizeEffectProxy<T extends AbstractSize>
        implements ImageExceptionThrowingConsumer<SourceImage, T> {

    private final Logger log;

    private final ImageCustomSizeEffect<T> imageCustomSizeEffect;

    @LogTimeExecuted("ImageCustomSizeEffectProxy#accept(sourceImage, targetSize)")
    @Override
    public void accept(@NonNull SourceImage sourceImage, @NonNull T targetSize) throws ImageException {

        final Dimension targetDimension = imageCustomSizeEffect
                .calcTargetDimension(sourceImage.bufferedImage(), targetSize);

        if (targetDimension.equals(sourceImage.dimension())) {

            log.debug("Dimensions are equal");
            return;
        }

        imageCustomSizeEffect.accept(sourceImage, targetDimension);
    }
}

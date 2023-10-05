package com.wilddev.image.services.editors.effects;

import com.wilddev.image.entities.settings.ResizeSize;
import com.wilddev.image.exceptions.images.ImageException;
import com.wilddev.image.processors.tasks.SourceImage;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.ImageWriterService;
import com.wilddev.image.services.editors.tools.ImageResizer;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImageResizeEffect extends AbstractImageCustomSizeEffect<ResizeSize> {

    public ImageResizeEffect(ImageResizer imageResizer,
                             ImageWriterService imageWriterService) {
        super(imageResizer, imageWriterService);
    }

    @LogTimeExecuted("ImageResizeEffect#calcTargetDimension(bufferedImage, targetDimension)")
    @Override
    public Dimension calcTargetDimension(@NonNull BufferedImage bufferedImage, @NonNull ResizeSize targetDimension) {

        int sourceWidth = bufferedImage.getWidth();
        int sourceHeight = bufferedImage.getHeight();

        Float targetScale = targetDimension.getScale();

        if (targetScale == null) {

            Integer targetWith = targetDimension.getWidth();
            Integer targetHeight = targetDimension.getHeight();

            if (targetWith == null && targetHeight == null)
                return new Dimension(sourceWidth, sourceHeight);

            double ratio = calcRatio(sourceWidth, sourceHeight);

            return targetWith == null ? new Dimension(calcWidth(targetHeight, ratio), targetHeight)
                    : new Dimension(targetWith, Objects.requireNonNullElseGet(targetHeight,
                    () -> calcHeight(targetWith, ratio)));
        }

        return new Dimension((int) (sourceWidth * targetScale), (int) (sourceHeight * targetScale));
    }

    @LogTimeExecuted("ImageResizeEffect#accept(sourceImage, targetDimension)")
    @Override
    public void accept(@NonNull SourceImage sourceImage, @NonNull Dimension targetDimension) throws ImageException {
        imageWriterService.write(sourceImage, imageResizer.resize(sourceImage.bufferedImage(), targetDimension));
    }
}

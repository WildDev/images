package com.wilddev.image.services.editors.tools;

import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.awt.image.BufferedImage;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImageResizer {

    @LogTimeExecuted("ImageResizer#resize(source, target)")
    public BufferedImage resize(@NonNull BufferedImage source, @NonNull Dimension target) {

        final int targetWidth = (int) target.getWidth();
        final int targetHeight = (int) target.getHeight();

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, source.getType());
        resizedImage.getGraphics().drawImage(source, 0, 0, targetWidth, targetHeight, null);

        return resizedImage;
    }
}

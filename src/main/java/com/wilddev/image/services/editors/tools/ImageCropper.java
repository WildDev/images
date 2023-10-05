package com.wilddev.image.services.editors.tools;

import com.wilddev.image.exceptions.images.ImageCroppingException;
import com.wilddev.image.services.MessageService;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.awt.image.BufferedImage;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@AllArgsConstructor
@Component
public class ImageCropper {

    private final MessageService messageService;

    private int calcNewPoint(double originalValue, int newValue) {
        return (int) (originalValue - newValue) / 2;
    }

    @LogTimeExecuted("ImageCropper#crop(image, target)")
    public BufferedImage crop(@NonNull BufferedImage image, @NonNull Dimension target) throws ImageCroppingException {

        final int targetWidth = (int) target.getWidth();
        final int targetHeight = (int) target.getHeight();

        try {
            return image.getSubimage(
                    calcNewPoint(image.getWidth(), targetWidth),
                    calcNewPoint(image.getHeight(), targetHeight),
                    targetWidth, targetHeight);

        } catch (Exception ex) {
            throw new ImageCroppingException(messageService.getMessage("exception.image.cropping.failed",
                    target.getWidth(), target.getHeight()), ex);
        }
    }
}

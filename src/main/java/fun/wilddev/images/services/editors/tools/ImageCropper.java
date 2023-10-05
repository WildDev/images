package fun.wilddev.images.services.editors.tools;

import fun.wilddev.images.exceptions.images.ImageCroppingException;
import fun.wilddev.spring.core.services.MessageService;

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

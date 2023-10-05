package fun.wilddev.images.services.editors.tools;

import java.awt.image.BufferedImage;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImageResizer {

    public BufferedImage resize(@NonNull BufferedImage source, @NonNull Dimension target) {

        final int targetWidth = (int) target.getWidth();
        final int targetHeight = (int) target.getHeight();

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, source.getType());
        resizedImage.getGraphics().drawImage(source, 0, 0, targetWidth, targetHeight, null);

        return resizedImage;
    }
}

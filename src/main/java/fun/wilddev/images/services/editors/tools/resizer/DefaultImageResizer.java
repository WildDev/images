package fun.wilddev.images.services.editors.tools.resizer;

import java.awt.image.BufferedImage;
import org.springframework.lang.NonNull;

import java.awt.*;

public class DefaultImageResizer implements ImageResizer {

    @Override
    public BufferedImage resize(@NonNull BufferedImage source, @NonNull Dimension target) {

        final int targetWidth = (int) target.getWidth();
        final int targetHeight = (int) target.getHeight();

        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, source.getType());
        resizedImage.getGraphics().drawImage(source, 0, 0, targetWidth, targetHeight, null);

        return resizedImage;
    }
}

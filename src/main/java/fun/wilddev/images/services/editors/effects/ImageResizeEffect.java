package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.entities.settings.ResizeSize;
import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.processors.tasks.SourceImage;
import fun.wilddev.images.services.ImageWriterService;
import fun.wilddev.images.services.editors.tools.resizer.ImageResizer;

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

    @Override
    public void accept(@NonNull SourceImage sourceImage, @NonNull Dimension targetDimension) throws FileException {
        imageWriterService.write(sourceImage, imageResizer.resize(sourceImage.bufferedImage(), targetDimension));
    }
}

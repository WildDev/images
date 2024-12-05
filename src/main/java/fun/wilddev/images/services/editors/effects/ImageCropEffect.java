package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.entities.settings.CropSize;
import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.processors.tasks.SourceImage;
import fun.wilddev.images.services.ImageWriterService;

import java.awt.image.BufferedImage;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fun.wilddev.images.exceptions.images.*;
import fun.wilddev.images.services.editors.tools.*;
import java.awt.*;

@Component
public class ImageCropEffect extends AbstractImageCustomSizeEffect<CropSize> {

    private final ImageCropper imageCropper;

    public ImageCropEffect(ImageResizer imageResizer,
                           ImageWriterService imageWriterService,
                           ImageCropper imageCropper) {

        super(imageResizer, imageWriterService);
        this.imageCropper = imageCropper;
    }

    private Integer getValue(Integer source, Integer target) {
        return target == null ? source : target;
    }

    private double calcRatio(Dimension dimension) {
        return calcRatio(dimension.getWidth(), dimension.getHeight());
    }

    private Dimension calcCoveredDimension(Dimension source, Dimension target) {

        double sourceRatio = calcRatio(source);
        double targetRatio = calcRatio(target);

        if (targetRatio == sourceRatio)
            return target;

        double targetWidth = target.getWidth();
        double targetHeight = target.getHeight();

        return targetRatio > sourceRatio ?
                new Dimension((int) targetWidth, (int) (targetWidth / sourceRatio)) :
                new Dimension((int) (targetHeight * sourceRatio), (int) targetHeight);
    }

    private BufferedImage resizeAndCrop(BufferedImage sourceBufferedImage, Dimension covered,
                                        Dimension target) throws ImageCroppingException {

        BufferedImage targetImage = imageResizer.resize(sourceBufferedImage, covered);
        return covered.equals(target) ? targetImage : imageCropper.crop(targetImage, target);
    }

    @Override
    public Dimension calcTargetDimension(@NonNull BufferedImage bufferedImage, @NonNull CropSize targetDimension) {

        int sourceWidth = bufferedImage.getWidth();
        int sourceHeight = bufferedImage.getHeight();

        Float targetRatio = targetDimension.getRatio();

        if (targetRatio == null)
            return new Dimension(getValue(sourceWidth, targetDimension.getWidth()),
                    getValue(sourceHeight, targetDimension.getHeight()));

        Integer targetWidth = targetDimension.getWidth();
        Integer targetHeight = targetDimension.getHeight();

        if (targetWidth == null && targetHeight == null)
            return new Dimension(calcWidth(sourceHeight, targetRatio), calcHeight(sourceWidth, targetRatio));
        else if (targetWidth == null)
            return new Dimension(calcWidth(targetHeight, targetRatio), targetHeight);
        else if (targetHeight == null)
            return new Dimension(targetWidth, calcHeight(targetWidth, targetRatio));
        else if (targetWidth <= sourceWidth && targetHeight <= sourceHeight)
            return new Dimension(targetWidth, targetHeight);
        else
            return new Dimension(sourceWidth, sourceHeight);
    }

    @Override
    public void accept(@NonNull SourceImage sourceImage, @NonNull Dimension targetDimension) throws FileException {

        final Dimension sourceDimension = sourceImage.dimension();
        final BufferedImage sourceBufferedImage = sourceImage.bufferedImage();

        Dimension covered = calcCoveredDimension(sourceDimension, targetDimension);

        imageWriterService.write(sourceImage, covered.equals(targetDimension) ?
                imageCropper.crop(sourceBufferedImage, targetDimension) :
                resizeAndCrop(sourceBufferedImage, covered, targetDimension));
    }
}

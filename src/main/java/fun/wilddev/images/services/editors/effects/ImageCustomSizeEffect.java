package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.entities.settings.AbstractSize;
import fun.wilddev.images.processors.tasks.SourceImage;

import java.awt.image.BufferedImage;

import java.awt.*;

public interface ImageCustomSizeEffect<T extends AbstractSize>
        extends FileExceptionThrowingConsumer<SourceImage, Dimension>, ImageEffect {

    Dimension calcTargetDimension(BufferedImage bufferedImage, T targetDimension);
}

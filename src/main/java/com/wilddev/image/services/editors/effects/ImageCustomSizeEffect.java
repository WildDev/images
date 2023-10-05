package com.wilddev.image.services.editors.effects;

import com.wilddev.image.models.ImageExceptionThrowingConsumer;
import com.wilddev.image.processors.tasks.SourceImage;

import java.awt.image.BufferedImage;

import com.wilddev.image.entities.settings.*;
import java.awt.*;

public interface ImageCustomSizeEffect<T extends AbstractSize>
        extends ImageExceptionThrowingConsumer<SourceImage, Dimension>, ImageEffect {

    Dimension calcTargetDimension(BufferedImage bufferedImage, T targetDimension);
}

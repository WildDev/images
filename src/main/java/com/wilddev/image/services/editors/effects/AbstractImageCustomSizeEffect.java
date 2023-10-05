package com.wilddev.image.services.editors.effects;

import com.wilddev.image.entities.settings.AbstractSize;
import com.wilddev.image.services.ImageWriterService;
import com.wilddev.image.services.editors.tools.ImageResizer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractImageCustomSizeEffect<T extends AbstractSize> implements ImageCustomSizeEffect<T> {

    protected final ImageResizer imageResizer;

    protected final ImageWriterService imageWriterService;

    protected double calcRatio(double width, double height) {
        return width / height;
    }

    protected int calcWidth(double height, double ratio) {
        return (int) (height * ratio);
    }

    protected int calcHeight(double width, double ratio) {
        return (int) (width / ratio);
    }
}

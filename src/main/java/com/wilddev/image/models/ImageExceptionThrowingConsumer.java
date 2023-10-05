package com.wilddev.image.models;

import com.wilddev.image.exceptions.images.ImageException;
import com.wilddev.image.sdk.models.ExceptionThrowingConsumer;

@FunctionalInterface
public interface ImageExceptionThrowingConsumer<T, Y> extends ExceptionThrowingConsumer<T, Y> {

    @Override
    void accept(T p1, Y p2) throws ImageException;
}

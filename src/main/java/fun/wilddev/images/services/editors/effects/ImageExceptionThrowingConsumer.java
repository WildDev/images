package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.exceptions.images.ImageException;

@FunctionalInterface
public interface ImageExceptionThrowingConsumer<T, Y> extends ExceptionThrowingConsumer<T, Y> {

    @Override
    void accept(T p1, Y p2) throws ImageException;
}

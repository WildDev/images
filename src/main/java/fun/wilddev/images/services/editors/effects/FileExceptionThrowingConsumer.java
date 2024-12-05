package fun.wilddev.images.services.editors.effects;

import fun.wilddev.images.exceptions.files.FileException;

@FunctionalInterface
public interface FileExceptionThrowingConsumer<T, Y> extends ExceptionThrowingConsumer<T, Y> {

    @Override
    void accept(T p1, Y p2) throws FileException;
}

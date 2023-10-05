package com.wilddev.image.sdk.models;

@FunctionalInterface
public interface ExceptionThrowingConsumer<T, Y> {

    void accept(T p1, Y p2) throws Exception;
}

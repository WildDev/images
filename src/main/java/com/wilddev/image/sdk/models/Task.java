package com.wilddev.image.sdk.models;

public interface Task<T> {

    void run(T context);
}

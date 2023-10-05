package com.wilddev.image.sdk.models;

public interface Sender<T> {

    void send(T p1) throws Exception;
}

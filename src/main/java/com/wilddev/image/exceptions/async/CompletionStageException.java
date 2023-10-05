package com.wilddev.image.exceptions.async;

import lombok.Getter;

@Getter
public abstract class CompletionStageException extends Exception {

    protected final Object payload;

    protected CompletionStageException(String message, Object payload) {

        super(message);
        this.payload = payload;
    }

    protected CompletionStageException(String message, Object payload, Throwable cause) {

        super(message, cause);
        this.payload = payload;
    }
}

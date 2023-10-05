package com.wilddev.image.sdk.async;

import java.util.concurrent.CompletableFuture;

public interface AsyncTask<T, Y> {

    CompletableFuture<Y> run(T context);
}

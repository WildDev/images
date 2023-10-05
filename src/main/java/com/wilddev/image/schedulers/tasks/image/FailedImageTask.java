package com.wilddev.image.schedulers.tasks.image;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.async.image.ImageFailedException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.services.ImageService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@AllArgsConstructor
@Component
public class FailedImageTask implements AsyncTask<Throwable, Void> {

    private final ImageService imageService;

    private Image extractPayload(ImageFailedException ex) {
        return (Image) ex.getPayload();
    }

    @LogTimeExecuted("FailedImageTask#run(throwable)")
    @Override
    public CompletableFuture<Void> run(@NonNull Throwable throwable) {

        if (throwable instanceof CompletionException) {

            Throwable cause = throwable.getCause();

            if (cause instanceof ImageFailedException)
                imageService.setFailed(extractPayload((ImageFailedException) cause));
        }

        return CompletableFuture.completedFuture(null);
    }
}

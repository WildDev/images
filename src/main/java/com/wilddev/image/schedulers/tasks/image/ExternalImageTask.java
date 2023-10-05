package com.wilddev.image.schedulers.tasks.image;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.async.image.ImageFailedException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.wilddev.image.services.*;

@AllArgsConstructor
@Component
public class ExternalImageTask implements AsyncTask<Image, Void> {

    private final ExternalImageService externalImageService;

    private final MessageService messageService;

    @LogTimeExecuted("ExternalImageTask#run(image)")
    @Async
    @Override
    public CompletableFuture<Void> run(@NonNull Image image) {

        try {
            externalImageService.download(image);
            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {
            return CompletableFuture.failedFuture(new ImageFailedException(
                    messageService.getMessage("exception.image.failed", image.getId()), image, ex));
        }
    }
}

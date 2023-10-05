package com.wilddev.image.schedulers.tasks.image;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.async.image.ImageFailedException;
import com.wilddev.image.exceptions.gridfs.GridFsMissingFileException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.processors.ImageProcessor;

import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.wilddev.image.services.*;

@AllArgsConstructor
@Component
public class DefaultImageTask implements AsyncTask<Image, Void> {

    private final GridFsService gridFsService;

    private final MessageService messageService;

    private final ImageProcessor imageProcessor;

    @LogTimeExecuted("DefaultImageTask#run(image)")
    @Async
    @Override
    public CompletableFuture<Void> run(@NonNull Image image) {

        GridFsResource resource = gridFsService.findById(image.getId().toString());

        try {
            if (resource.exists()) {

                imageProcessor.process(ImageIO.read(resource.getInputStream()), image);
                return CompletableFuture.completedFuture(null);
            }

            throw new GridFsMissingFileException(messageService
                    .getMessage("exception.file.gridfs.not.found", image.getId()));

        } catch (Exception ex) {
            return CompletableFuture.failedFuture(new ImageFailedException(
                    messageService.getMessage("exception.image.failed", image.getId()), image, ex));
        }
    }
}

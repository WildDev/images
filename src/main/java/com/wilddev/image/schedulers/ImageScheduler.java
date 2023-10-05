package com.wilddev.image.schedulers;

import com.wilddev.image.entities.Image;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.sdk.logics.ThreadedCollectionProcessor;
import com.wilddev.image.sdk.schedulers.CollectionProcessingScheduler;
import com.wilddev.image.schedulers.feed.factory.DistributedFeedFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.wilddev.image.schedulers.feed.*;
import com.wilddev.image.schedulers.tasks.image.*;

@Slf4j
@Component
public class ImageScheduler extends CollectionProcessingScheduler<Image> {

    private final DefaultImageTask defaultImageTask;

    private final ExternalImageTask externalImageTask;

    private final FailedImageTask failedImageTask;

    public ImageScheduler(DistributedFeedFactory distributedFeedFactory,
                          ImagesFeed imagesFeed,
                          DefaultImageTask defaultImageTask,
                          ExternalImageTask externalImageTask,
                          FailedImageTask failedImageTask) {

        super(new ThreadedCollectionProcessor<>(log,
                distributedFeedFactory.getObject(imagesFeed)));

        this.defaultImageTask = defaultImageTask;
        this.externalImageTask = externalImageTask;
        this.failedImageTask = failedImageTask;
    }

    private AsyncTask<Image, Void> selectTask(Image image) {
        return StringUtils.hasText(image.getSourceUrl()) ? externalImageTask : defaultImageTask;
    }

    @LogTimeExecuted("ImageScheduler#run")
    @Scheduled(fixedRateString = "${image.processor.rate}")
    @Override
    public void run() {

        collectionProcessor.process(image -> selectTask(image)
                .run(image).exceptionallyCompose(failedImageTask::run));
    }
}

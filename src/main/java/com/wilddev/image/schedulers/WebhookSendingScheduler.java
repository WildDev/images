package com.wilddev.image.schedulers;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.logics.ThreadedCollectionProcessor;
import com.wilddev.image.sdk.schedulers.CollectionProcessingScheduler;
import com.wilddev.image.schedulers.feed.factory.DistributedFeedFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wilddev.image.schedulers.feed.*;
import com.wilddev.image.schedulers.tasks.webhook.*;

@Slf4j
@Component
public class WebhookSendingScheduler extends CollectionProcessingScheduler<Webhook> {

    private final WebhookSendingTask webhookSendingTask;

    private final WebhookRetryTask webhookRetryTask;

    private final WebhookFailedTask webhookFailedTask;

    public WebhookSendingScheduler(DistributedFeedFactory distributedFeedFactory,
                                   WebhooksFeed webhooksFeed,
                                   WebhookSendingTask webhookSendingTask,
                                   WebhookRetryTask webhookRetryTask,
                                   WebhookFailedTask webhookFailedTask) {

        super(new ThreadedCollectionProcessor<>(log,
                distributedFeedFactory.getObject(webhooksFeed)));

        this.webhookSendingTask = webhookSendingTask;
        this.webhookRetryTask = webhookRetryTask;
        this.webhookFailedTask = webhookFailedTask;
    }

    @LogTimeExecuted("WebhookSendingScheduler#run")
    @Scheduled(fixedRateString = "${image.webhook.sender.rate}")
    @Override
    public void run() {

        log.debug("Webhook sending scheduler is started");

        collectionProcessor.process(webhook -> webhookSendingTask.run(webhook)
                .exceptionallyCompose(webhookRetryTask::run)
                .exceptionallyCompose(webhookFailedTask::run));
    }
}

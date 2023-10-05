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

    private final RetryWebhookTask retryWebhookTask;

    private final FailedWebhookTask failedWebhookTask;

    public WebhookSendingScheduler(DistributedFeedFactory distributedFeedFactory,
                                   WebhooksFeed webhooksFeed,
                                   WebhookSendingTask webhookSendingTask,
                                   RetryWebhookTask retryWebhookTask,
                                   FailedWebhookTask failedWebhookTask) {

        super(new ThreadedCollectionProcessor<>(log,
                distributedFeedFactory.getObject(webhooksFeed)));

        this.webhookSendingTask = webhookSendingTask;
        this.retryWebhookTask = retryWebhookTask;
        this.failedWebhookTask = failedWebhookTask;
    }

    @LogTimeExecuted("WebhookSendingScheduler#run")
    @Scheduled(fixedRateString = "${image.webhook.sender.rate}")
    @Override
    public void run() {

        collectionProcessor.process(webhook -> webhookSendingTask.run(webhook)
                .exceptionallyCompose(retryWebhookTask::run)
                .exceptionallyCompose(failedWebhookTask::run));
    }
}

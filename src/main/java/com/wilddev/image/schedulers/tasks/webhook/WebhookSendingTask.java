package com.wilddev.image.schedulers.tasks.webhook;

import com.wilddev.image.enums.WebhookType;
import com.wilddev.image.exceptions.async.webhook.WebhookFailedException;
import com.wilddev.image.exceptions.webhooks.UnknownWebhookTypeException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.services.MessageService;
import com.wilddev.image.services.webhooks.WebhookService;
import com.wilddev.image.services.webhooks.sender.ImageWebhookSender;

import java.util.concurrent.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.wilddev.image.entities.webhooks.*;

@Slf4j
@AllArgsConstructor
@Component
public class WebhookSendingTask implements AsyncTask<Webhook, Void> {

    private final ImageWebhookSender imageWebhookSender;

    private final WebhookService webhookService;

    private final MessageService messageService;

    @LogTimeExecuted("WebhookSendingTask#run(webhook)")
    @Async
    @Override
    public CompletableFuture<Void> run(@NonNull Webhook webhook) {

        final WebhookType type = webhook.getType();

        try {
            switch (type) {
                case IMAGE_READY, IMAGE_FAILED -> imageWebhookSender.send(webhook);
                default -> throw new UnknownWebhookTypeException(messageService
                        .getMessage("exception.webhook.unknown.type", type));
            }

            webhookService.setSent(webhook);

            return CompletableFuture.completedFuture(null);

        } catch (Exception ex) {
            return CompletableFuture.failedFuture(new WebhookFailedException(
                    messageService.getMessage("exception.webhook.failed", type), webhook, ex));
        }
    }
}

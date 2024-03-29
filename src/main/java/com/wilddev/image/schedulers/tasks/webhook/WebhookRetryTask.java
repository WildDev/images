package com.wilddev.image.schedulers.tasks.webhook;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.services.MessageService;
import com.wilddev.image.services.webhooks.WebhookService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.wilddev.image.exceptions.async.webhook.*;
import java.util.concurrent.*;

@AllArgsConstructor
@Component
public class WebhookRetryTask implements AsyncTask<Throwable, Void> {

    private final WebhookService webhookService;

    private final MessageService messageService;

    private Webhook extractPayload(WebhookFailedException ex) {
        return (Webhook) ex.getPayload();
    }

    @LogTimeExecuted("WebhookRetryTask#run(throwable)")
    @Override
    public CompletableFuture<Void> run(@NonNull Throwable throwable) {

        if (throwable instanceof CompletionException &&
                throwable.getCause() instanceof final WebhookFailedException ex) {

            final Webhook webhook = extractPayload(ex);

            if (!webhookService.retry(webhook))
                return CompletableFuture.failedFuture(new WebhookNoTriesLeftException(
                        messageService.getMessage("exception.webhook.tries.over", webhook.getId()), webhook));
        }

        return CompletableFuture.completedFuture(null);
    }
}

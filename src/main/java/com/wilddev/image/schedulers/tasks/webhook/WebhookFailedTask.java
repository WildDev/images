package com.wilddev.image.schedulers.tasks.webhook;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.exceptions.async.webhook.WebhookNoTriesLeftException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.async.AsyncTask;
import com.wilddev.image.services.webhooks.WebhookService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@AllArgsConstructor
@Component
public class WebhookFailedTask implements AsyncTask<Throwable, Void> {

    private final WebhookService webhookService;

    private Webhook extractPayload(WebhookNoTriesLeftException ex) {
        return (Webhook) ex.getPayload();
    }

    @LogTimeExecuted("WebhookFailedTask#run(throwable)")
    @Override
    public CompletableFuture<Void> run(@NonNull Throwable throwable) {

        if (throwable instanceof CompletionException &&
                throwable.getCause() instanceof final WebhookNoTriesLeftException ex)
            webhookService.setFailed(extractPayload(ex));

        return CompletableFuture.completedFuture(null);
    }
}

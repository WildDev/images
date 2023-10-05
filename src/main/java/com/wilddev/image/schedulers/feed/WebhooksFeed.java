package com.wilddev.image.schedulers.feed;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.services.webhooks.WebhookService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebhooksFeed extends AbstractNewItemsFeed<Webhook> {

    public WebhooksFeed(@Value("${webhook.feed.batch-size}") Integer batchSize,
                        WebhookService webhookService) {
        super(batchSize, webhookService);
    }
}

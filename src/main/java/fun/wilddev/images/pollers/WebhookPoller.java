package fun.wilddev.images.pollers;

import fun.wilddev.images.config.props.WebhookProps;
import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.queues.DefaultWebhookQueue;
import fun.wilddev.images.services.webhooks.WebhookService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebhookPoller extends Poller<Webhook> {

    public WebhookPoller(WebhookProps webhookProps, WebhookService webhookService, DefaultWebhookQueue queue) {
        super(log, webhookProps.pollSize(), webhookService, queue);
    }
}

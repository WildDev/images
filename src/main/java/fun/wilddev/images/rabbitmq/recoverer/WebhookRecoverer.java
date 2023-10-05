package fun.wilddev.images.rabbitmq.recoverer;

import com.fasterxml.jackson.databind.ObjectMapper;

import fun.wilddev.images.rabbitmq.data.WebhookData;
import fun.wilddev.images.services.webhooks.WebhookService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebhookRecoverer extends Recoverer<WebhookData> {

    public WebhookRecoverer(ObjectMapper objectMapper, WebhookService webhookService) {
        super(log, objectMapper, webhookService, "Webhook {} is failed", WebhookData.class);
    }
}

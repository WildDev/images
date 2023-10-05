package fun.wilddev.images.handlers;

import fun.wilddev.images.enums.WebhookType;
import fun.wilddev.images.models.ImageWebhook;
import fun.wilddev.images.services.webhooks.WebhookService;
import fun.wilddev.images.services.webhooks.sender.http.WebhookHttp;

import fun.wilddev.images.exceptions.webhooks.UnknownWebhookTypeException;
import fun.wilddev.images.exceptions.webhooks.WebhookDeliveryException;
import fun.wilddev.spring.core.services.MessageService;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WebhookSendingHandler implements Handler<ImageWebhook> {

    private final WebhookHttp webhookHttp;

    private final WebhookService webhookService;

    private final MessageService messageService;

    @Override
    public void handle(@NonNull ImageWebhook webhook) throws UnknownWebhookTypeException,
            WebhookDeliveryException {

        final WebhookType type = webhook.type();

        switch (type) {
            case IMAGE_READY, IMAGE_FAILED -> webhookHttp.send(webhook);
            default -> throw new UnknownWebhookTypeException(messageService
                    .getMessage("exception.webhook.unknown.type", type));
        }

        webhookService.setSent(webhook.id());
    }
}

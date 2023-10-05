package fun.wilddev.images.rabbitmq.webhook;

import fun.wilddev.images.exceptions.webhooks.UnknownWebhookTypeException;
import fun.wilddev.images.handlers.WebhookSendingHandler;
import fun.wilddev.images.models.ImageWebhook;
import fun.wilddev.images.rabbitmq.RabbitContainerFactories;
import fun.wilddev.images.rabbitmq.data.WebhookData;

import lombok.AllArgsConstructor;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WebhookListener {

    private final WebhookSendingHandler webhookSendingHandler;

    @RabbitListener(containerFactory = RabbitContainerFactories.WEBHOOKS,
            queues = "${spring.rabbitmq.queue.webhooks.name}",
            concurrency = "${spring.rabbitmq.queue.webhooks.concurrency}")
    public void send(@NonNull WebhookData message) throws Exception {

        try {
            webhookSendingHandler.handle(new ImageWebhook(message.getId(), message.getImageId(), message.getType()));
        } catch (UnknownWebhookTypeException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}

package fun.wilddev.images.rabbitmq.webhook;

import fun.wilddev.images.models.Tick;
import fun.wilddev.images.rabbitmq.GcTickListener;
import fun.wilddev.images.services.webhooks.WebhookService;

import lombok.AllArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WebhookGcTickListener implements GcTickListener {

    private final WebhookService webhookService;

    @RabbitListener(queues = "${spring.rabbitmq.queue.webhook-gc-tick.name}")
    @Override
    public void tick(@NonNull Tick tick) {
        webhookService.collectExpired(tick.timestamp());
    }
}

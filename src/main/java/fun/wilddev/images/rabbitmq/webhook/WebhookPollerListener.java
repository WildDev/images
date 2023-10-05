package fun.wilddev.images.rabbitmq.webhook;

import fun.wilddev.images.models.Tick;
import fun.wilddev.images.pollers.WebhookPoller;
import fun.wilddev.images.rabbitmq.PollerListener;

import lombok.AllArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class WebhookPollerListener implements PollerListener {

    private final WebhookPoller webhookPoller;

    @RabbitListener(queues = "${spring.rabbitmq.queue.webhook-poll-tick.name}")
    @Override
    public void poll(@NonNull Tick tick) {
        webhookPoller.poll();
    }
}

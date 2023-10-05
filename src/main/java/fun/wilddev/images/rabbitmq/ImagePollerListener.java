package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.models.Tick;
import fun.wilddev.images.pollers.ImagePoller;

import lombok.AllArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ImagePollerListener implements PollerListener {

    private final ImagePoller imagePoller;

    @RabbitListener(queues = "${spring.rabbitmq.queue.image-poll-tick.name}")
    @Override
    public void poll(@NonNull Tick tick) {
        imagePoller.poll();
    }
}

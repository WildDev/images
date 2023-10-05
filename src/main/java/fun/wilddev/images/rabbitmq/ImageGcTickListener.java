package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.models.Tick;
import fun.wilddev.images.services.ImageService;

import lombok.AllArgsConstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ImageGcTickListener implements GcTickListener {

    private final ImageService imageService;

    @RabbitListener(queues = "${spring.rabbitmq.queue.image-gc-tick.name}")
    @Override
    public void tick(@NonNull Tick tick) {
        imageService.collectExpired(tick.timestamp());
    }
}

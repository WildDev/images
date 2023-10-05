package fun.wilddev.images.queues;

import fun.wilddev.images.services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@AllArgsConstructor
public abstract class AbstractImageQueue implements ImageQueue {

    protected final String exchange;

    protected final String routingKey;

    protected final RabbitTemplate rabbitTemplate;

    protected final ImageService imageService;
}

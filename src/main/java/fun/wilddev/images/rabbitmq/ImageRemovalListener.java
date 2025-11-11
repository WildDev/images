package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.handlers.ImageRemovalHandler;
import fun.wilddev.images.rabbitmq.data.ImageRemovalData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ImageRemovalListener {

    private final ImageRemovalHandler imageRemovalHandler;

    @RabbitListener(containerFactory = RabbitContainerFactories.IMAGES,
            queues = "${spring.rabbitmq.queue.image-removal.name}",
            concurrency = "${spring.rabbitmq.queue.image-removal.concurrency}")
    public void process(@NonNull ImageRemovalData message) throws Exception {

        imageRemovalHandler.handle(message);
        log.debug("Image {} is deleted", message.getId());
    }
}

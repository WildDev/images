package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.exceptions.gridfs.GridFsMissingFileException;
import fun.wilddev.images.handlers.ImageDelayedProcessingHandler;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.rabbitmq.data.DefaultImageData;

import lombok.AllArgsConstructor;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DefaultImageListener {

    private final ImageDelayedProcessingHandler imageDelayedProcessingHandler;

    @RabbitListener(containerFactory = RabbitContainerFactories.DEFAULT_IMAGES,
            queues = "${spring.rabbitmq.queue.default-images.name}",
            concurrency = "${spring.rabbitmq.queue.default-images.concurrency}")
    public void process(@NonNull DefaultImageData message) throws Exception {

        try {
            imageDelayedProcessingHandler.handle(new FileMeta(message.getId(), message.getContentType()));
        } catch (GridFsMissingFileException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}

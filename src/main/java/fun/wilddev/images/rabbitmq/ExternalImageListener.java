package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.exceptions.UnsupportedContentTypeException;
import fun.wilddev.images.exceptions.files.FileSizeLimitExceededException;
import fun.wilddev.images.handlers.ImageDownloadingHandler;
import fun.wilddev.images.models.ExternalImage;
import fun.wilddev.images.rabbitmq.data.ExternalImageData;

import lombok.AllArgsConstructor;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ExternalImageListener {

    private final ImageDownloadingHandler imageDownloadingHandler;

    @RabbitListener(containerFactory = RabbitContainerFactories.EXTERNAL_IMAGES,
            queues = "${spring.rabbitmq.queue.external-images.name}",
            concurrency = "${spring.rabbitmq.queue.external-images.concurrency}")
    public void process(@NonNull ExternalImageData message) throws Exception {

        try {
            imageDownloadingHandler.handle(new ExternalImage(message.getId(), message.getSourceUrl()));
        } catch (UnsupportedContentTypeException | FileSizeLimitExceededException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}

package fun.wilddev.images.queues;

import fun.wilddev.images.entities.Image;
import fun.wilddev.images.rabbitmq.data.DefaultImageData;
import fun.wilddev.images.services.ImageService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fun.wilddev.images.config.props.*;

@Component
public class DefaultImageQueue extends AbstractImageQueue {

    public DefaultImageQueue(ExchangeProps exchangeProps, RoutingKeyMappingProps routingKeyMappingProps,
                             RabbitTemplate rabbitTemplate, ImageService imageService) {
        super(exchangeProps.image(), routingKeyMappingProps.defaultImages().name(), rabbitTemplate, imageService);
    }

    @Transactional
    @Override
    public void push(@NonNull Image image) {

        final String id = image.getId();

        rabbitTemplate.convertAndSend(exchange, routingKey, new DefaultImageData(id, image.getContentType()));
        imageService.setQueued(id);
    }
}

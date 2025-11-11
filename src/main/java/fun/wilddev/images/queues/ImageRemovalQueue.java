package fun.wilddev.images.queues;

import fun.wilddev.images.rabbitmq.data.ImageRemovalData;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fun.wilddev.images.config.props.*;

@Component
public class ImageRemovalQueue implements Queue<String> {

    private final String exchange;

    private final String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public ImageRemovalQueue(ExchangeProps exchange, RoutingKeyMappingProps routingKeyMappingProps,
                             RabbitTemplate rabbitTemplate) {

        this.exchange = exchange.image();
        this.routingKey = routingKeyMappingProps.imageRemoval().name();
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public void push(@NonNull String id) {
        rabbitTemplate.convertAndSend(exchange, routingKey, new ImageRemovalData(id));
    }
}

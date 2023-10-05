package fun.wilddev.images.config.rabbitmq;

import fun.wilddev.images.config.props.QueueProps;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueNameFactory {

    public String create(@NonNull QueueProps queueProps) {
        return queueProps.name() + "-dead-letter";
    }
}

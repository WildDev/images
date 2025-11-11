package fun.wilddev.images.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rabbitmq.queue")
public record QueueMappingProps(QueueProps imagePollTick,
                                QueueProps imageGcTick,
                                QueueProps defaultImages,
                                QueueProps externalImages,
                                QueueProps webhookPollTick,
                                QueueProps webhookGcTick,
                                QueueProps webhooks,
                                QueueProps imageRemoval) {

}

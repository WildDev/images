package fun.wilddev.images.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rabbitmq.routing-key")
public record RoutingKeyMappingProps(RoutingKeyProps defaultImages,
                                     RoutingKeyProps externalImages,
                                     RoutingKeyProps webhooks,
                                     RoutingKeyProps secondTick,
                                     RoutingKeyProps minuteTick) {

}

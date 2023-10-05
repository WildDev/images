package fun.wilddev.images.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rabbitmq.exchange")
public record ExchangeProps(String image, String imageDeadLetter,
                            String webhook, String webhookDeadLetter,
                            String scheduler) {

}

package fun.wilddev.images.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webhook")
public record WebhookProps(Integer pollSize, String timeout, String url) {

}

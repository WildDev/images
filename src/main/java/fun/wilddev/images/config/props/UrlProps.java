package fun.wilddev.images.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("url")
public record UrlProps(Integer connectTimeout, Integer readTimeout) {

}

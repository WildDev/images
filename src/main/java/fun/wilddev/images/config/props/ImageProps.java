package fun.wilddev.images.config.props;

import fun.wilddev.images.enums.ImageType;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("image")
public record ImageProps(Integer pollSize, String timeout, Set<ImageType> allowedTypes) {

}

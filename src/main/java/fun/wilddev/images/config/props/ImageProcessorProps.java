package fun.wilddev.images.config.props;

import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import fun.wilddev.images.enums.*;

@ConfigurationProperties("image.processor")
public record ImageProcessorProps(Set<ImageType> editableTypes,
                                  Set<ImageTaskType> tasks) {

}

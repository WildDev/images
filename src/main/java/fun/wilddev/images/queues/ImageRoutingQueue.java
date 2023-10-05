package fun.wilddev.images.queues;

import fun.wilddev.images.entities.Image;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@AllArgsConstructor
@Component
public class ImageRoutingQueue implements ImageQueue {

    private final DefaultImageQueue defaultImageQueue;

    private final ExternalImageQueue externalImageQueue;

    @Override
    public void push(@NonNull Image image) {
        (StringUtils.hasText(image.getSourceUrl()) ? externalImageQueue : defaultImageQueue).push(image);
    }
}

package fun.wilddev.images.pollers;

import fun.wilddev.images.config.props.ImageProps;
import fun.wilddev.images.entities.Image;
import fun.wilddev.images.queues.ImageRoutingQueue;
import fun.wilddev.images.services.ImageService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImagePoller extends Poller<Image> {

    public ImagePoller(ImageProps imageProps, ImageService imageService, ImageRoutingQueue queue) {
        super(log, imageProps.pollSize(), imageService, queue);
    }
}

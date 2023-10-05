package fun.wilddev.images.rabbitmq.recoverer;

import com.fasterxml.jackson.databind.ObjectMapper;

import fun.wilddev.images.services.ImageFailureSetter;
import fun.wilddev.images.rabbitmq.data.ImageData;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageRecoverer extends Recoverer<ImageData> {

    public ImageRecoverer(ObjectMapper objectMapper, ImageFailureSetter imageFailureSetter) {
        super(log, objectMapper, imageFailureSetter, "Image {} is failed", ImageData.class);
    }
}

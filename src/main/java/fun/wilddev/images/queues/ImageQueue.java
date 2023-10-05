package fun.wilddev.images.queues;

import fun.wilddev.images.entities.Image;
import org.springframework.lang.NonNull;

public interface ImageQueue extends Queue<Image> {

    void push(@NonNull Image image);
}

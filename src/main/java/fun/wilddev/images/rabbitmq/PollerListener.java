package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.models.Tick;
import org.springframework.lang.NonNull;

public interface PollerListener {

    void poll(@NonNull Tick tick);
}

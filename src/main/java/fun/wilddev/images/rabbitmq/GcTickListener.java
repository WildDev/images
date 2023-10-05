package fun.wilddev.images.rabbitmq;

import fun.wilddev.images.models.Tick;

public interface GcTickListener {

    void tick(Tick tick);
}

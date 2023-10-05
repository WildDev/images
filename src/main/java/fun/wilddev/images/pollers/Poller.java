package fun.wilddev.images.pollers;

import fun.wilddev.images.queues.Queue;
import fun.wilddev.images.services.Slicer;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.springframework.data.domain.Slice;

@AllArgsConstructor
public abstract class Poller<T> {

    protected final Logger log;

    protected final Integer pollSize;

    protected final Slicer<T> slicer;

    protected final Queue<T> queue;

    public void poll() {

        Slice<T> items = slicer.sliceNew(pollSize);

        if (items.hasContent()) {

            items.get().forEach(message -> {

                try {
                    queue.push(message);
                } catch (Exception ex) {
                    log.error("Failed to queue an item", ex);
                }
            });

            return;
        }

        log.debug("No items to process, skipping ...");
    }
}

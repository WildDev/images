package fun.wilddev.images.experimental;

import java.util.List;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.function.*;

@AllArgsConstructor
public class CollectionProcessor<T> {

    private final Logger log;

    public void process(Supplier<List<T>> feed, Consumer<List<T>> consumer) {

        List<T> items = feed.get();

        if (CollectionUtils.isEmpty(items)) {

            log.debug("No items to process, skipping ...");
            return;
        }

        consumer.accept(items);
    }
}

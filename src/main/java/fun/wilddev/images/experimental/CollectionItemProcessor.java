package fun.wilddev.images.experimental;

import java.util.List;
import org.slf4j.Logger;

import java.util.function.*;

public class CollectionItemProcessor<T> {

    private final CollectionProcessor<T> processor;

    public CollectionItemProcessor(Logger log) {
        this.processor = new CollectionProcessor<>(log);
    }

    public void process(Supplier<List<T>> feed, Consumer<T> consumer) {
        processor.process(feed, (items) -> items.forEach(consumer));
    }
}

package com.wilddev.image.sdk.logics;

import com.wilddev.image.schedulers.feed.NewItemsFeed;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.slf4j.Logger;

public class ThreadedCollectionProcessor<T> {

    private final Logger log;

    private final NewItemsFeed<T> newItemsFeed;

    private final CollectionProcessor<T> collectionProcessor;

    public ThreadedCollectionProcessor(Logger log, NewItemsFeed<T> newItemsFeed) {

        this.collectionProcessor = new CollectionProcessor<>(log);

        this.log = log;
        this.newItemsFeed = newItemsFeed;
    }

    public void process(Function<T, CompletableFuture<Void>> processor) {

        collectionProcessor.process(newItemsFeed::stealNew, items -> {

            final int size = items.size();
            CompletableFuture<?>[] futures = new CompletableFuture[size];

            IntStream.range(0, size).forEach(i -> futures[i] = processor.apply(items.get(i)));

            try {
                CompletableFuture.allOf(futures).join();
            } catch (Exception ex) {

                log.error("Failed to process the items", ex);
                return;
            }

            log.debug("{} items processed", size);
        });
    }
}

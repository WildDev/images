package com.wilddev.image.sdk.schedulers;

import com.wilddev.image.sdk.logics.ThreadedCollectionProcessor;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class CollectionProcessingScheduler<T> implements Runnable {

    protected final ThreadedCollectionProcessor<T> collectionProcessor;
}

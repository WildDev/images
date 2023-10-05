package com.wilddev.image.schedulers.feed;

import com.wilddev.image.concurrent.ExclusiveLock;
import com.wilddev.image.services.LockService;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DistributedFeed<T> implements NewItemsFeed<T> {

    private final String lockKey;

    private final LockService lockService;

    private final NewItemsFeed<T> delegate;

    @Override
    public List<T> stealNew() {

        ExclusiveLock lock = lockService.obtain(lockKey);

        try {
            return lock.tryLock() ? delegate.stealNew() : null;
        } finally {
            lock.unlock();
        }
    }
}

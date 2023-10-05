package com.wilddev.image.schedulers.feed.factory;

import com.wilddev.image.services.LockService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wilddev.image.schedulers.feed.*;
import java.util.stream.*;
import org.springframework.lang.*;

@Component
public class DistributedFeedFactory {

    private final static String LOCK_KEY_SPLITTER_PATTERN = "(?=[A-Z])";

    private final static String LOCK_KEY_JOINER = "-";

    private final Boolean lockEnabled;

    private final LockService lockService;

    public DistributedFeedFactory(@Value("${lock.enabled}") Boolean lockEnabled,
                                  @Nullable LockService lockService) {

        this.lockEnabled = lockEnabled;
        this.lockService = lockService;
    }

    public <T> NewItemsFeed<T> getObject(@NonNull NewItemsFeed<T> target) {
        return lockEnabled ? new DistributedFeed<>(composeLockKey(target),
                lockService, target) : target;
    }

    private String composeLockKey(Object target) {
        return Stream.of(target.getClass().getName().split(LOCK_KEY_SPLITTER_PATTERN))
                .map(String::toLowerCase)
                .collect(Collectors.joining(LOCK_KEY_JOINER));
    }
}

package com.wilddev.image.services;

import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.concurrent.ExclusiveLock;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty("lock.enabled")
@Service
public class LockService {

    private final static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    private final Integer timeout;

    private final LockRegistry lockRegistry;

    public LockService(@Value("${lock.timeout}") Integer timeout,
                       LockRegistry lockRegistry) {

        this.timeout = timeout;
        this.lockRegistry = lockRegistry;
    }

    @LogTimeExecuted("LockService#obtain(key)")
    public ExclusiveLock obtain(@NonNull String key) {

        try {
            return new ExclusiveLock(timeout, TIME_UNIT, lockRegistry.obtain(key));
        } catch (Exception ex) {

            log.error("Failed to obtain a lock for key {}", key, ex);
            return null;
        }
    }
}

package com.wilddev.image.sdk.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ExclusiveLock(long time, TimeUnit unit, Lock lock) {

    public boolean tryLock() {

        try {
            return lock.tryLock(time, unit);
        } catch (Exception ex) {

            log.warn("Failed to acquire a lock", ex);
            return false;
        }
    }

    public void unlock() {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "ExclusiveLock{" +
                "time=" + time +
                ", unit=" + unit +
                ", lock=" + lock +
                '}';
    }
}

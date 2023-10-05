package com.wilddev.image.services;

import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.time.LocalDateTime;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ExpirationDateService {

    @LogTimeExecuted("ExpirationDateService#calc(bound, minutes)")
    public LocalDateTime calc(@NonNull LocalDateTime bound, @NonNull Integer minutes) {
        return bound.plusMinutes(minutes);
    }
}

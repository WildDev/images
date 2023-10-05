package com.wilddev.image.schedulers;

import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.ImageService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ImageTimeoutSetter {

    private final ImageService imageService;

    @LogTimeExecuted("ImageTimeoutSetter#run")
    @Scheduled(cron = "${image.timeout.setter.cron}")
    public void run() {
        log.info("{} images were expired", imageService.collectExpired());
    }
}

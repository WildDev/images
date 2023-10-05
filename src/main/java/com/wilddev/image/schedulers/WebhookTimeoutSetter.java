package com.wilddev.image.schedulers;

import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.webhooks.WebhookService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class WebhookTimeoutSetter {

    private final WebhookService webhookService;

    @LogTimeExecuted("WebhookTimeoutSetter#run")
    @Scheduled(cron = "${image.webhook.timeout.setter.cron}")
    public void run() {
        log.info("{} webhooks were expired", webhookService.collectExpired());
    }
}

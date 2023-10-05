package com.wilddev.image.services.webhooks;

import com.wilddev.image.entities.webhooks.meta.WebhookMeta;
import com.wilddev.image.enums.WebhookType;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.models.ExpiredRecordsCollector;
import com.wilddev.image.services.ReservationService;
import com.wilddev.image.repositories.WebhookRepository;
import com.wilddev.image.services.ExpirationDateService;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wilddev.image.enums.*;
import com.wilddev.image.entities.webhooks.*;
import java.util.*;
import org.springframework.data.domain.*;

@Slf4j
@Service
public class WebhookService implements ReservationService<Webhook>, ExpiredRecordsCollector {

    private final Integer tries;

    private final Integer triesDistance;

    private final Integer timeout;

    private final ExpirationDateService expirationDateService;

    private final WebhookRepository webhookRepository;

    public WebhookService(@Value("${image.webhook.timeout}") Integer timeout,
                          @Value("${image.webhook.tries}") Integer tries,
                          @Value("${image.webhook.tries.distance}") Integer triesDistance,
                          ExpirationDateService expirationDateService,
                          WebhookRepository webhookRepository) {

        this.timeout = timeout;
        this.tries = tries;
        this.triesDistance = triesDistance;
        this.expirationDateService = expirationDateService;
        this.webhookRepository = webhookRepository;
    }

    private LocalDateTime calcNextTry() {
        return LocalDateTime.now().plusSeconds(triesDistance);
    }

    @LogTimeExecuted("WebhookService#add(type, meta)")
    @Transactional
    public void add(@NonNull WebhookType type, @NonNull WebhookMeta meta) {

        LocalDateTime now = LocalDateTime.now();

        Webhook webhook = new Webhook(type, WebhookStatus.NEW, tries - 1,
                now, expirationDateService.calc(now, timeout), meta);

        webhookRepository.save(webhook);

        log.debug("Added webhook {}", webhook.getId());
    }

    @LogTimeExecuted("WebhookService#listNew(batchSize)")
    @Transactional
    @Override
    public List<Webhook> listNew(int batchSize) {
        return webhookRepository.findByStatusIn(Set.of(WebhookStatus.NEW, WebhookStatus.RETRY),
                PageRequest.of(0, batchSize, Sort.Direction.ASC, "created"));
    }

    @LogTimeExecuted("WebhookService#reserve(webhooks)")
    @Transactional
    @Override
    public void reserve(@NonNull List<Webhook> webhooks) {

        webhooks.forEach(webhook -> webhook.setStatus(WebhookStatus.SENDING));
        webhookRepository.saveAll(webhooks);
    }

    @LogTimeExecuted("WebhookService#retry(webhook)")
    @Transactional
    public boolean retry(@NonNull Webhook webhook) {

        Integer triesLeft = webhook.getTriesLeft();

        if (triesLeft <= 0) {

            log.debug("No tries left for webhook {}", webhook.getId());
            return false;
        }

        webhook.setStatus(WebhookStatus.RETRY);

        webhook.setTriesLeft(--triesLeft);
        webhook.setLastTried(LocalDateTime.now());
        webhook.setNextTry(calcNextTry());

        webhookRepository.save(webhook);

        return true;
    }

    @LogTimeExecuted("WebhookService#setSent(webhook)")
    @Transactional
    public void setSent(@NonNull Webhook webhook) {

        webhook.setStatus(WebhookStatus.SENT);
        webhook.setSent(LocalDateTime.now());

        webhookRepository.save(webhook);
    }

    @LogTimeExecuted("WebhookService#setFailed(webhook)")
    @Transactional
    public void setFailed(@NonNull Webhook webhook) {

        webhook.setStatus(WebhookStatus.FAILED);
        webhookRepository.save(webhook);
    }

    @LogTimeExecuted("WebhookService#collectExpired")
    @Override
    public long collectExpired() {
        return webhookRepository.findAndSetExpiredByStatusAndExpiresBefore(WebhookStatus.NEW,
                LocalDateTime.now(), WebhookStatus.EXPIRED);
    }
}

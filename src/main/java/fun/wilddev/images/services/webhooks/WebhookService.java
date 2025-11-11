package fun.wilddev.images.services.webhooks;

import fun.wilddev.images.config.props.WebhookProps;
import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.entities.webhooks.meta.WebhookMeta;
import fun.wilddev.images.repositories.WebhookRepository;
import fun.wilddev.spring.core.services.date.FutureCalculator;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fun.wilddev.images.enums.*;
import fun.wilddev.images.services.*;
import org.springframework.data.domain.*;

@Slf4j
@Service
public class WebhookService implements Slicer<Webhook>, FailureSetter {

    private final String timeout;

    private final FutureCalculator futureCalculator;

    private final WebhookRepository webhookRepository;

    public WebhookService(WebhookProps webhookProps,
                          FutureCalculator futureCalculator,
                          WebhookRepository webhookRepository) {

        this.timeout = webhookProps.timeout();
        this.futureCalculator = futureCalculator;
        this.webhookRepository = webhookRepository;
    }

    @Transactional
    public void add(@NonNull WebhookType type, @NonNull WebhookMeta meta) {

        LocalDateTime now = LocalDateTime.now();

        Webhook webhook = new Webhook(type, WebhookStatus.NEW, now,
                futureCalculator.calc(now, timeout), meta);

        webhookRepository.save(webhook);

        log.debug("Added webhook {}", webhook.getId());
    }

    @Transactional
    @Override
    public Slice<Webhook> sliceNew(int size) {
        return webhookRepository.findByStatus(WebhookStatus.NEW,
                PageRequest.of(0, size, Sort.Direction.ASC, "added"));
    }

    @Transactional
    public void setQueued(@NonNull String id) {
        webhookRepository.findAndSetStatusById(id, WebhookStatus.QUEUED);
    }

    @Transactional
    public void setSent(@NonNull String id) {
        webhookRepository.findAndSetStatusAndSentById(id, WebhookStatus.SENT, LocalDateTime.now());
    }

    @Transactional
    @Override
    public void setFailed(@NonNull String id) {
        webhookRepository.findAndSetStatusById(id, WebhookStatus.FAILED);
    }

    @Transactional
    public void deleteByImageId(@NonNull String imageId) {
        log.debug("{} images deleted", webhookRepository.deleteByMetaImageId(imageId));
    }

    @Transactional
    public void collectExpired(@NonNull LocalDateTime bound) {

        log.info("{} records were expired", webhookRepository
                .findAndSetExpiredByStatusAndExpiresBefore(WebhookStatus.NEW, bound, WebhookStatus.EXPIRED));
    }
}

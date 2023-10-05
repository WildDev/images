package com.wilddev.image.repositories;

import com.wilddev.image.entities.webhooks.Webhook;
import com.wilddev.image.enums.WebhookStatus;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import java.util.*;
import org.springframework.data.mongodb.repository.*;

public interface WebhookRepository extends MongoRepository<Webhook, String> {

    List<Webhook> findByStatusIn(Set<WebhookStatus> status, Pageable pageable);

    @Update("{ $set: { status: ?2 } }")
    long findAndSetExpiredByStatusAndExpiresBefore(WebhookStatus status, LocalDateTime bound, WebhookStatus target);
}

package fun.wilddev.images.repositories;

import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.enums.WebhookStatus;

import java.time.LocalDateTime;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;

public interface WebhookRepository extends MongoRepository<Webhook, String> {

    // _id_1 +
    @Update("{ $set: { 'status': ?1, 'sent': { $date: ?2 } } }")
    void findAndSetStatusAndSentById(String id, WebhookStatus status, LocalDateTime sent);

    @Update("{ $set: { 'status': ?1 } }")
    void findAndSetStatusById(String id, WebhookStatus status);

    // status_1_expires_1 +
    Slice<Webhook> findByStatus(WebhookStatus status, Pageable pageable);

    @Update("{ $set: { 'status': ?2 } }")
    long findAndSetExpiredByStatusAndExpiresBefore(WebhookStatus status, LocalDateTime bound, WebhookStatus target);
}

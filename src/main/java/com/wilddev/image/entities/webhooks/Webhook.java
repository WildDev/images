package com.wilddev.image.entities.webhooks;

import com.wilddev.image.entities.webhooks.meta.WebhookMeta;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import org.springframework.data.annotation.Id;

import com.wilddev.image.enums.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Document("webhooks")
public class Webhook {

    @Id
    private ObjectId id;

    @Field
    private WebhookType type;

    @Field
    private WebhookStatus status;

    @Field
    private Integer triesLeft;

    @Field
    private LocalDateTime nextTry;

    @Field
    private LocalDateTime created;

    @Field
    private LocalDateTime expires;

    @Field
    private LocalDateTime lastTried;

    @Field
    private LocalDateTime sent;

    @Field
    private WebhookMeta meta;

    public Webhook(WebhookType type, WebhookStatus status, Integer triesLeft,
                   LocalDateTime created, LocalDateTime expires, WebhookMeta meta) {

        this.type = type;
        this.status = status;
        this.triesLeft = triesLeft;
        this.created = created;
        this.expires = expires;
        this.meta = meta;
    }
}

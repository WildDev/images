package fun.wilddev.images.entities.webhooks;

import fun.wilddev.images.entities.webhooks.meta.WebhookMeta;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import fun.wilddev.images.enums.*;
import lombok.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

@CompoundIndex(name = "status_1_expires_1", def = "{ 'status': 1, 'expires': 1 }")
@CompoundIndex(name = "meta.image_id_1", def = "{ 'meta.image_id': 1 }")

@NoArgsConstructor
@Setter
@Getter
@ToString
@Document("webhooks")
public class Webhook {

    @Id
    private String id;

    @Field
    private WebhookType type;

    @Field
    private WebhookStatus status;

    @Field
    private LocalDateTime added;

    @Field
    private LocalDateTime expires;

    @Field
    private LocalDateTime sent;

    @Field
    private WebhookMeta meta;

    public Webhook(WebhookType type, WebhookStatus status, LocalDateTime added,
                   LocalDateTime expires, WebhookMeta meta) {

        this.type = type;
        this.status = status;
        this.added = added;
        this.expires = expires;
        this.meta = meta;
    }
}

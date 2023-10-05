package fun.wilddev.images.entities.webhooks;

import fun.wilddev.images.entities.webhooks.meta.WebhookMeta;
import fun.wilddev.images.enums.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

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

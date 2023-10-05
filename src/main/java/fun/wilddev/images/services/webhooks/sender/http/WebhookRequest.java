package fun.wilddev.images.services.webhooks.sender.http;

import com.fasterxml.jackson.annotation.JsonProperty;

import fun.wilddev.images.enums.WebhookType;
import fun.wilddev.images.models.ImageWebhook;

import org.springframework.lang.NonNull;

import lombok.*;

@Setter
@Getter
@ToString
public class WebhookRequest {

    @JsonProperty
    private WebhookType type;

    @JsonProperty
    private String imageId;

    public WebhookRequest(@NonNull ImageWebhook source) {

        this.type = source.type();
        this.imageId = source.imageId();
    }
}

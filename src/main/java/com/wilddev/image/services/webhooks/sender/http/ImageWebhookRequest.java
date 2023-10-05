package com.wilddev.image.services.webhooks.sender.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wilddev.image.enums.WebhookType;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class ImageWebhookRequest extends WebhookRequest {

    @JsonProperty
    private WebhookType type;

    @JsonProperty
    private String imageId;
}

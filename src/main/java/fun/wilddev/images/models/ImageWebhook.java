package fun.wilddev.images.models;

import fun.wilddev.images.enums.WebhookType;

public record ImageWebhook(String id, String imageId, WebhookType type) {

}

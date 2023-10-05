package fun.wilddev.images.services.webhooks.sender;

import fun.wilddev.images.exceptions.webhooks.WebhookDeliveryException;
import fun.wilddev.images.experimental.models.Sender;
import fun.wilddev.images.models.ImageWebhook;

public interface WebhookDeliveryService extends Sender<ImageWebhook> {

    void send(ImageWebhook webhook) throws WebhookDeliveryException;
}

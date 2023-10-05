package fun.wilddev.images.queues;

import fun.wilddev.images.entities.webhooks.Webhook;
import fun.wilddev.images.entities.webhooks.meta.ImageReference;
import fun.wilddev.images.exceptions.webhooks.MalformedWebhookMetadataException;
import fun.wilddev.images.rabbitmq.data.WebhookData;
import fun.wilddev.images.services.webhooks.WebhookService;
import fun.wilddev.images.services.webhooks.meta.ImageReferenceReader;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import fun.wilddev.images.config.props.*;

@Slf4j
@Component
public class DefaultWebhookQueue implements Queue<Webhook> {

    private final String exchange;

    private final String routingKey;

    private final RabbitTemplate rabbitTemplate;

    private final WebhookService webhookService;

    private final ImageReferenceReader metadataReader;

    public DefaultWebhookQueue(ExchangeProps exchangeProps, RoutingKeyMappingProps routingKeyMappingProps,
                               RabbitTemplate rabbitTemplate, WebhookService webhookService,
                               ImageReferenceReader metadataReader) {

        this.exchange = exchangeProps.webhook();
        this.routingKey = routingKeyMappingProps.webhooks().name();

        this.rabbitTemplate = rabbitTemplate;
        this.webhookService = webhookService;
        this.metadataReader = metadataReader;
    }

    @Transactional
    @Override
    public void push(@NonNull Webhook webhook) throws MalformedWebhookMetadataException {

        final String id = webhook.getId();

        ImageReference webhookMeta = metadataReader.read(webhook);
        log.debug("Webhook metadata: {}", webhookMeta);

        rabbitTemplate.convertAndSend(exchange, routingKey,
                new WebhookData(id, webhook.getType(), webhookMeta.getImageId()));

        webhookService.setQueued(id);
    }
}

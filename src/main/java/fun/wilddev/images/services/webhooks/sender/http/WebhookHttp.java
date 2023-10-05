package fun.wilddev.images.services.webhooks.sender.http;

import fun.wilddev.images.config.props.WebhookProps;
import fun.wilddev.images.enums.WebhookType;
import fun.wilddev.images.exceptions.webhooks.WebhookDeliveryException;
import fun.wilddev.images.models.ImageWebhook;
import fun.wilddev.images.services.webhooks.sender.WebhookDeliveryService;
import fun.wilddev.spring.core.services.MessageService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.http.*;
import org.springframework.web.client.*;

@Slf4j
@Service
public class WebhookHttp implements WebhookDeliveryService {

    private final String url;

    private final RestTemplate restTemplate;

    private final MessageService messageService;

    public WebhookHttp(WebhookProps webhookProps,
                       RestTemplate restTemplate,
                       MessageService messageService) {

        this.url = webhookProps.url();
        this.restTemplate = restTemplate;
        this.messageService = messageService;
    }

    @Override
    public void send(@NonNull ImageWebhook webhook) throws WebhookDeliveryException {

        final WebhookType type = webhook.type();

        try {
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(url,
                    new WebhookRequest(webhook), Object.class);

            HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

            if (httpStatusCode.is2xxSuccessful()) {

                log.info("Webhook {} for image {} is acknowledged", type, webhook.imageId());
                return;
            }

            throw new HttpClientErrorException(httpStatusCode,
                    messageService.getMessage("exception.http.wrong.status"));

        } catch (RestClientException ex) {

            if (ex instanceof final HttpClientErrorException http4xx)
                log.error("Response details: {}", http4xx.getResponseBodyAsString());

            throw new WebhookDeliveryException(messageService
                    .getMessage("exception.webhook.delivery.failed", type, url), ex);
        }
    }
}

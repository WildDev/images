package com.wilddev.image.services.webhooks.sender.http;

import com.wilddev.image.exceptions.webhooks.WebhookDeliveryException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.http.HttpService;
import com.wilddev.image.services.MessageService;
import com.wilddev.image.services.webhooks.sender.WebhookDeliveryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.http.*;
import org.springframework.web.client.*;

@Slf4j
@Service
public class ImageWebhookHttp extends HttpService implements WebhookDeliveryService<ImageWebhookRequest> {

    public ImageWebhookHttp(@Value("${image.webhook.url}") String url,
                            RestTemplate restTemplate,
                            MessageService messageService) {
        super(url, restTemplate, messageService);
    }

    @LogTimeExecuted("ImageWebhookHttp#send(request)")
    @Override
    public void send(@NonNull ImageWebhookRequest request) throws WebhookDeliveryException {

        final String imageId = request.getImageId();

        try {
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, request, Object.class);
            HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

            if (httpStatusCode.is2xxSuccessful()) {

                log.info("Webhook {} for image {} is acknowledged", request.getType(), imageId);
                return;
            }

            throw new HttpClientErrorException(httpStatusCode,
                    messageService.getMessage("exception.http.wrong.status"));

        } catch (RestClientException ex) {

            if (ex instanceof HttpClientErrorException)
                log.error("Response details: {}", ((HttpClientErrorException) ex).getResponseBodyAsString());

            throw new WebhookDeliveryException(messageService
                    .getMessage("exception.webhook.delivery.failed", request.getType(), url), ex);
        }
    }
}

package com.wilddev.image.sdk.http;

import com.wilddev.image.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public abstract class HttpService {

    protected final String url;

    protected final RestTemplate restTemplate;

    protected final MessageService messageService;
}

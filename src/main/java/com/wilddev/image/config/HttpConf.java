package com.wilddev.image.config;

import org.springframework.web.client.RestTemplate;

import org.springframework.context.annotation.*;

@Configuration
public class HttpConf {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.wilddev.image.services;

import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.net.*;

@Service
public class UrlService {

    private final Integer connectTimeout;

    private final Integer readTimeout;

    public UrlService(@Value("${url.connect.timeout}") Integer connectTimeout,
                      @Value("${url.read.timeout}") Integer readTimeout) {

        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @LogTimeExecuted("UrlService#getConnection(url)")
    public URLConnection getConnection(@NonNull String url) throws IOException, URISyntaxException {

        URLConnection conn = new URI(url).toURL().openConnection();

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);

        conn.connect();

        return conn;
    }
}

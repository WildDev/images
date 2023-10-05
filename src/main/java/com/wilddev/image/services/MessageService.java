package com.wilddev.image.services;

import java.util.Locale;

import lombok.AllArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MessageService {

    private final static String DEFAULT_MESSAGE = "Unknown message code";

    private final MessageSource messageSource;

    public String getMessage(@NonNull String code, @NonNull Object... args) {
        return messageSource.getMessage(code, args, DEFAULT_MESSAGE, Locale.getDefault());
    }

    public String getMessage(@NonNull String code) {
        return getMessage(code, new Object[] { });
    }
}

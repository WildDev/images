package com.wilddev.image.sdk.mappers;

import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;
import org.springframework.util.*;

@Component
public class MultiValueMapper {

    public <T, Y> MultiValueMap<T, Y> map(@NonNull Map<T, Y> map) {
        return new LinkedMultiValueMap<>(map.entrySet().stream().collect(Collectors
                .toMap(Map.Entry::getKey, e -> Collections.singletonList(e.getValue()))));
    }
}

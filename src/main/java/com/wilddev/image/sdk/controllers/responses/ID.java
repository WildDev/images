package com.wilddev.image.sdk.controllers.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ID<T> {

    @JsonProperty
    protected T id;
}

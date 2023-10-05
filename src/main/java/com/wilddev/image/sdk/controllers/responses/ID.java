package com.wilddev.image.sdk.controllers.responses;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ID<T> {

    protected T id;
}

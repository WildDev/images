package fun.wilddev.images.controllers.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class ID<T> {

    @JsonProperty
    private T id;
}

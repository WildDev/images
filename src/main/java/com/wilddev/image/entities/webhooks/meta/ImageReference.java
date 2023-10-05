package com.wilddev.image.entities.webhooks.meta;

import com.wilddev.image.entities.Image;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ImageReference implements WebhookMeta {

    @DocumentReference
    private Image image;
}

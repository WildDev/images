package fun.wilddev.images.entities.webhooks.meta;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ImageReference implements WebhookMeta {

    @Field(name = "image_id", targetType = FieldType.OBJECT_ID)
    private String imageId;
}

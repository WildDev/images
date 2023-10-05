package fun.wilddev.images.rabbitmq.data;

import lombok.*;

@NoArgsConstructor
@ToString(callSuper = true)
public abstract class ImageData extends RefData {

    protected ImageData(String id) {
        super(id);
    }
}

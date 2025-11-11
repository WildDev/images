package fun.wilddev.images.rabbitmq.data;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class ImageRemovalData extends RefData {

    public ImageRemovalData(String id) {
        super(id);
    }
}

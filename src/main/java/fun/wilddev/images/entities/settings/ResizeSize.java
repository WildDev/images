package fun.wilddev.images.entities.settings;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Setter
@Getter
@ToString(callSuper = true)
@Document("resize_sizes")
public class ResizeSize extends AbstractSize {

    @Field
    private Float scale;
}

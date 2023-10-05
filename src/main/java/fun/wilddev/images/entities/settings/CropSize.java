package fun.wilddev.images.entities.settings;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Setter
@Getter
@Document("crop_sizes")
public class CropSize extends AbstractSize {

    @Field
    private Float ratio;

    @Override
    public String toString() {

        String str = super.toString();

        if (ratio != null)
            str += " ratio=" + ratio;

        return str;
    }
}

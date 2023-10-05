package fun.wilddev.images.rabbitmq.data;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class DefaultImageData extends ImageData {

    private String contentType;

    public DefaultImageData(String id, String contentType) {

        super(id);
        this.contentType = contentType;
    }
}

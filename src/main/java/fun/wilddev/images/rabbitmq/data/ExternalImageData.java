package fun.wilddev.images.rabbitmq.data;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class ExternalImageData extends ImageData {

    private String sourceUrl;

    public ExternalImageData(String id, String sourceUrl) {

        super(id);
        this.sourceUrl = sourceUrl;
    }
}

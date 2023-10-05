package fun.wilddev.images.rabbitmq.data;

import fun.wilddev.images.enums.WebhookType;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class WebhookData extends RefData {

    private WebhookType type;

    private String imageId;

    public WebhookData(String id, WebhookType type, String imageId) {

        super(id);

        this.type = type;
        this.imageId = imageId;
    }
}

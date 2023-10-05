package fun.wilddev.images.entities;

import fun.wilddev.images.enums.ImageStatus;
import fun.wilddev.images.models.FileMeta;

import java.time.LocalDateTime;

import java.awt.*;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Document("images")
public class Image {

    @Id
    private String id;

    @Field
    private String sourceUrl;

    @Field
    private Integer width;

    @Field
    private Integer height;

    @Field
    private ImageStatus status;

    @Field
    private String contentType;

    @Field
    private Boolean multiSize;

    @Field
    private LocalDateTime added;

    @Field
    private LocalDateTime expires;

    @Field
    private LocalDateTime processed;

    @Field(targetType = FieldType.OBJECT_ID)
    private String sourceId;

    private Image(ImageStatus status, Boolean multiSize, LocalDateTime added,
                  LocalDateTime expires) {

        this.status = status;
        this.multiSize = multiSize;
        this.added = added;
        this.expires = expires;
    }

    public Image(String sourceUrl, ImageStatus status, Boolean multiSize,
                 LocalDateTime added, LocalDateTime expires) {

        this(status, multiSize, added, expires);
        this.sourceUrl = sourceUrl;
    }

    public Image(FileMeta source, Dimension dimension, ImageStatus status,
                 Boolean multiSize, LocalDateTime added, LocalDateTime expires) {

        this(status, multiSize, added, expires);

        this.width = (int) dimension.getWidth();
        this.height = (int) dimension.getHeight();

        this.contentType = source.contentType();
        this.sourceId = source.id();
    }

    public Image(ImageStatus status, String contentType, Boolean multiSize,
                 LocalDateTime added, LocalDateTime expires) {

        this(status, multiSize, added, expires);
        this.contentType = contentType;
    }
}

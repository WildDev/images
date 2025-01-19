package fun.wilddev.images.entities;

import fun.wilddev.images.enums.ImageStatus;
import fun.wilddev.images.models.FileMeta;

import java.time.LocalDateTime;

import java.awt.*;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.*;

@CompoundIndexes({
        @CompoundIndex(name = "source_id_1_status_1_width_1_height_1",
                def = "{ 'source_id': 1, 'status': 1, 'width': 1, 'height': 1 }"),
        @CompoundIndex(name = "source_id_1_status_1_height_1",
                def = "{ 'source_id': 1, 'status': 1, 'height': 1 }"),
        @CompoundIndex(name = "status_1_expires_1", def = "{ 'status': 1, 'expires': 1 }")
})

@NoArgsConstructor
@Setter
@Getter
@ToString
@Document("images")
public class Image {

    @Id
    private String id;

    @Field("source_url")
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

    @Field(name = "source_id", targetType = FieldType.OBJECT_ID)
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

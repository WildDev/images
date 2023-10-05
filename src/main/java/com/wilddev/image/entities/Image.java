package com.wilddev.image.entities;

import com.wilddev.image.enums.ImageStatus;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

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
    private ObjectId id;

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
    private LocalDateTime created;

    @Field
    private LocalDateTime expires;

    @Field
    private LocalDateTime processed;

    @DocumentReference
    private Image source;

    @Version
    private Long version;

    private Image(ImageStatus status, Boolean multiSize, LocalDateTime created,
                  LocalDateTime expires) {

        this.status = status;
        this.multiSize = multiSize;
        this.created = created;
        this.expires = expires;
    }

    public Image(String sourceUrl, ImageStatus status, Boolean multiSize,
                 LocalDateTime created, LocalDateTime expires) {

        this(status, multiSize, created, expires);
        this.sourceUrl = sourceUrl;
    }

    public Image(Image source, Dimension dimension, ImageStatus status,
                 Boolean multiSize, LocalDateTime created, LocalDateTime expires) {

        this(status, multiSize, created, expires);

        this.width = (int) dimension.getWidth();
        this.height = (int) dimension.getHeight();

        this.sourceUrl = source.getSourceUrl();
        this.contentType = source.getContentType();
        this.source = source;
    }

    public Image(ImageStatus status, String contentType, Boolean multiSize,
                 LocalDateTime created, LocalDateTime expires) {

        this(status, multiSize, created, expires);
        this.contentType = contentType;
    }
}

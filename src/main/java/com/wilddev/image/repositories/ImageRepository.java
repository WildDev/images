package com.wilddev.image.repositories;

import com.wilddev.image.entities.Image;
import com.wilddev.image.enums.ImageStatus;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import java.util.*;
import org.springframework.data.mongodb.repository.*;

public interface ImageRepository extends MongoRepository<Image, String> {

    Optional<Image> findByIdAndStatus(String id, ImageStatus status);

    Optional<Image> findBySourceIdAndStatusAndWidthAndHeight(String id, ImageStatus status,
                                                             Integer width, Integer height);

    List<Image> findBySourceIdAndStatusAndWidth(String id, ImageStatus status, Integer width);

    List<Image> findBySourceIdAndStatusAndHeight(String id, ImageStatus status, Integer height);

    List<Image> findByStatus(ImageStatus status, Pageable pageable);

    @Update("{ $set: { status: ?2 } }")
    long findAndSetExpiredByStatusAndExpiresBefore(ImageStatus status, LocalDateTime bound, ImageStatus target);
}

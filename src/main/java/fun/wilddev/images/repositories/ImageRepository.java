package fun.wilddev.images.repositories;

import fun.wilddev.images.entities.Image;
import fun.wilddev.images.enums.ImageStatus;

import java.time.LocalDateTime;

import java.util.*;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;

public interface ImageRepository extends MongoRepository<Image, String> {

    // _id_1 +
    Optional<Image> findByIdAndStatus(String id, ImageStatus status);

    @Update("{ $set: { 'status': ?1, 'processed': { $date: ?2 } } }")
    void findAndSetStatusAndProcessedById(String id, ImageStatus status, LocalDateTime processed);

    @Update("{ $set: { 'status': ?1 } }")
    void findAndSetStatusById(String id, ImageStatus status);

    // source_id_1_status_1_width_1_height_1 +
    Optional<Image> findBySourceIdAndStatusAndWidthAndHeight(String id, ImageStatus status,
                                                             Integer width, Integer height);
    // source_id +
    List<Image> findBySourceId(String sourceId);

    List<Image> findBySourceIdAndStatusAndWidth(String id, ImageStatus status, Integer width);

    // source_id_1_status_1_height_1 +
    List<Image> findBySourceIdAndStatusAndHeight(String id, ImageStatus status, Integer height);

    // status_1_expires_1 +
    Slice<Image> findByStatus(ImageStatus status, Pageable pageable);

    @Update("{ $set: { 'status': ?2 } }")
    long findAndSetExpiredByStatusAndExpiresBefore(ImageStatus status, LocalDateTime bound, ImageStatus target);
}

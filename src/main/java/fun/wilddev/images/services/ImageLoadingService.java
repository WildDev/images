package fun.wilddev.images.services;

import fun.wilddev.images.entities.Image;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class ImageLoadingService {

    private final ImageService imageService;

    private final GridFsService gridFsService;

    private Optional<Image> pickFirst(List<Image> images) {
        return images.stream().findFirst();
    }

    public GridFsResource findOne(@NonNull String id, Integer width, Integer height) {

        Optional<Image> imageOptional = Optional.empty();

        if (width != null && height != null)
            imageOptional = imageService.findProcessed(id, width, height);
        else if (width != null)
            imageOptional = pickFirst(imageService.findProcessedByWidth(id, width));
        else if (height != null)
            imageOptional = pickFirst(imageService.findProcessedByHeight(id, height));

        if (imageOptional.isEmpty())
            imageOptional = imageService.findProcessed(id);

        if (imageOptional.isEmpty()) {

            log.debug("No versions found for image {}", id);
            return null;
        }

        return gridFsService.findById(imageOptional.get().getId());
    }
}

package com.wilddev.image.services;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.images.ImageStorageException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Slf4j
@AllArgsConstructor
@Service
public class CustomizedImageService {

    private final ImageService imageService;

    private final GridFsService gridFsService;

    private final MessageService messageService;

    @LogTimeExecuted("CustomizedImageService#store(file, dimension, sourceImage)")
    @Transactional
    public void store(@NonNull File file, @NonNull Dimension dimension,
                      @NonNull Image sourceImage) throws ImageStorageException {

        String id = null;

        try {
            Image croppedImage = imageService.add(sourceImage, dimension);

            id = croppedImage.getId().toString();

            gridFsService.store(id, croppedImage.getContentType(), file);
            imageService.setProcessed(croppedImage);

        } catch (Exception ex) {

            if (id != null) {

                gridFsService.delete(id);
                log.debug("GridFS file {} is deleted", id);
            }

            throw new ImageStorageException(messageService.getMessage("exception.image.version.storage.failed",
                    dimension.getWidth(), dimension.getHeight(), sourceImage.getId()), ex);
        }
    }
}

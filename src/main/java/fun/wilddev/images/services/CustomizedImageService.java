package fun.wilddev.images.services;

import fun.wilddev.images.entities.Image;
import fun.wilddev.images.exceptions.images.ImageStorageException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.spring.core.services.MessageService;

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

    @Transactional
    public void store(@NonNull File file, @NonNull Dimension dimension,
                      @NonNull FileMeta fileMeta) throws ImageStorageException {

        String id = null;

        try {
            Image croppedImage = imageService.add(fileMeta, dimension);

            id = croppedImage.getId();

            gridFsService.store(id, croppedImage.getContentType(), file);
            imageService.setProcessed(id);

        } catch (Exception ex) {

            if (id != null) {

                gridFsService.delete(id);
                log.debug("GridFS file {} is deleted", id);
            }

            throw new ImageStorageException(messageService.getMessage("exception.image.version.storage.failed",
                    dimension.getWidth(), dimension.getHeight(), fileMeta.id()), ex);
        }
    }
}

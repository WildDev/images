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

        final int width = (int) dimension.getWidth();
        final int height = (int) dimension.getHeight();

        String id = null;

        try {
            Image croppedImage = imageService.storeSampleResult(fileMeta, dimension);
            id = croppedImage.getId();

            gridFsService.store(id, croppedImage.getContentType(), file);

            log.info("{}x{} sample of image {} is processed", width, height, fileMeta.id());

        } catch (Exception ex) {

            if (id != null) {

                gridFsService.delete(id);
                log.warn("GridFS file {} is deleted", id);
            }

            throw new ImageStorageException(messageService
                    .getMessage("exception.image.version.storage.failed", width, height, fileMeta.id()), ex);
        }
    }
}

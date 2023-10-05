package com.wilddev.image.services;

import com.wilddev.image.entities.Image;
import com.wilddev.image.exceptions.images.ImageException;
import com.wilddev.image.processors.tasks.SourceImage;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.editors.tools.BufferedImageWriter;

import java.awt.image.BufferedImage;
import java.io.File;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;

@AllArgsConstructor
@Service
public class ImageWriterService {

    private final BufferedImageWriter bufferedImageWriter;

    private final CustomizedImageService customizedImageService;

    private final ImageService imageService;

    private final FileService fileService;

    private Dimension getDimension(BufferedImage bufferedImage) {
        return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    @LogTimeExecuted("ImageWriterService#write(source, bufferedImage)")
    public void write(@NonNull SourceImage source, @NonNull BufferedImage bufferedImage) throws ImageException {

        final Image sourceImage = source.image();

        File tempFile = null;

        try {
            tempFile = bufferedImageWriter.write(bufferedImage,
                    imageService.getFormatName(sourceImage.getContentType()));

            customizedImageService.store(tempFile, getDimension(bufferedImage), sourceImage);

        } finally {
            fileService.delete(tempFile);
        }
    }
}

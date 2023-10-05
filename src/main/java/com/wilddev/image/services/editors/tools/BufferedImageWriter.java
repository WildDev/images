package com.wilddev.image.services.editors.tools;

import com.wilddev.image.exceptions.images.ImageWritingException;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.wilddev.image.services.*;

@AllArgsConstructor
@Component
public class BufferedImageWriter {

    private final MessageService messageService;

    private final FileService fileService;

    @LogTimeExecuted("BufferedImageWriter#write(bufferedImage, formatName)")
    public File write(@NonNull BufferedImage bufferedImage,
                      @NonNull String formatName) throws ImageWritingException {

        try {
            File tempFile = fileService.createTempFile();
            ImageIO.write(bufferedImage, formatName, tempFile);

            return tempFile;

        } catch (Exception ex) {
            throw new ImageWritingException(messageService
                    .getMessage("exception.image.writing.failed", formatName), ex);
        }
    }
}

package fun.wilddev.images.services.editors.tools;

import fun.wilddev.images.exceptions.images.ImageWritingException;
import fun.wilddev.images.services.FileService;
import fun.wilddev.spring.core.services.MessageService;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class BufferedImageTmpFileWriter {

    private final MessageService messageService;

    private final FileService fileService;

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

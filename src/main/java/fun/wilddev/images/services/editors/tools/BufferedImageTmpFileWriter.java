package fun.wilddev.images.services.editors.tools;

import fun.wilddev.images.exceptions.images.ImageWritingException;
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

    public void write(@NonNull File tempFile, @NonNull BufferedImage bufferedImage,
                      @NonNull String formatName) throws ImageWritingException {

        try {
            ImageIO.write(bufferedImage, formatName, tempFile);
        } catch (Exception ex) {
            throw new ImageWritingException(messageService
                    .getMessage("exception.image.writing.failed", formatName), ex);
        }
    }
}

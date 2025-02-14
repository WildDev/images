package fun.wilddev.images.services;

import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.processors.tasks.SourceImage;
import fun.wilddev.images.services.editors.tools.BufferedImageTmpFileWriter;

import java.awt.image.BufferedImage;
import java.io.File;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.awt.*;

@AllArgsConstructor
@Service
public class ImageWriterService {

    private final BufferedImageTmpFileWriter bufferedImageTmpFileWriter;

    private final CustomizedImageService customizedImageService;

    private final ImageService imageService;

    private final FileService fileService;

    private Dimension getDimension(BufferedImage bufferedImage) {
        return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
    }

    public void write(@NonNull SourceImage source, @NonNull BufferedImage bufferedImage) throws FileException {

        final FileMeta fileMeta = source.fileMeta();

        File tempFile = null;

        try {
            tempFile = fileService.createTempFile();

            bufferedImageTmpFileWriter.write(tempFile, bufferedImage,
                    imageService.getFormatName(fileMeta.contentType()));

            customizedImageService.store(tempFile, getDimension(bufferedImage), fileMeta);

        } finally {

            if (tempFile != null)
                fileService.delete(tempFile);
        }
    }
}

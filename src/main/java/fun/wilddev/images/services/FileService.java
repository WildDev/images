package fun.wilddev.images.services;

import fun.wilddev.images.exceptions.files.FileWriteException;
import fun.wilddev.spring.core.services.MessageService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;

@Slf4j
@AllArgsConstructor
@Service
public class FileService {

    private final static String PREFIX = "tmp";

    private final static String SUFFIX = ".image";

    private final MessageService messageService;

    public File createTempFile() throws FileWriteException {

        try {
            Path path = Files.createTempFile(PREFIX, SUFFIX);
            log.debug("Created {}", path);

            return path.toFile();

        } catch (IOException ex) {
            throw new FileWriteException(messageService.getMessage("exception.image.tmp.create.failed"), ex);
        }
    }

    public void delete(File file) {

        if (file == null)
            return;

        try {
            Files.delete(file.toPath());
        } catch (Exception ex) {
            log.error("Failed to delete temporary file {}", file, ex);
        }
    }
}

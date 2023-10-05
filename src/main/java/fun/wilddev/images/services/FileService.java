package fun.wilddev.images.services;

import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class FileService {

    private final static String PREFIX = "tmp";

    private final static String SUFFIX = ".image";

    public File createTempFile() throws IOException {
        return Files.createTempFile(PREFIX, SUFFIX).toFile();
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

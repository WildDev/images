package com.wilddev.image.services;

import com.wilddev.image.sdk.aop.LogTimeExecuted;
import java.nio.file.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class FileService {

    private final static String PREFIX = "news";

    private final static String SUFFIX = ".image";

    @LogTimeExecuted("FileService#createTempFile")
    public File createTempFile() throws IOException {
        return Files.createTempFile(PREFIX, SUFFIX).toFile();
    }

    @LogTimeExecuted("FileService#delete(file)")
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

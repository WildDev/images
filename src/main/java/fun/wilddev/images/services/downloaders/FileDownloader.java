package fun.wilddev.images.services.downloaders;

import fun.wilddev.images.exceptions.files.FileDownloadingException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.services.downloaders.callbacks.FileReadyCallback;
import fun.wilddev.spring.core.services.MessageService;

import java.net.URLConnection;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.util.StreamUtils;

import java.io.*;
import fun.wilddev.images.services.*;

@AllArgsConstructor
public class FileDownloader {

    private final FileService fileService;

    private final GridFsService gridFsService;

    private final MessageService messageService;

    private final FileReadyCallback fileReadyCallback;

    public void download(@NonNull URLConnection conn, @NonNull String id) throws FileDownloadingException {

        final String contentType = conn.getContentType();

        try {
            File tempFile = fileService.createTempFile();

            try (InputStream is = new BufferedInputStream(conn.getInputStream());
                 OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {

                StreamUtils.copy(is, os);

                gridFsService.store(id, contentType, tempFile);
                fileReadyCallback.onSuccess(tempFile, new FileMeta(id, contentType));

            } finally {
                fileService.delete(tempFile);
            }

        } catch (Exception ex) {
            throw new FileDownloadingException(messageService.getMessage("exception.file.downloading.failed", id), ex);
        }
    }
}

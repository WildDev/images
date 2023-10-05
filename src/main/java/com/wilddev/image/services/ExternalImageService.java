package com.wilddev.image.services;

import com.wilddev.image.entities.Image;
import com.wilddev.image.enums.ImageType;
import com.wilddev.image.exceptions.images.ImageDownloadingException;
import com.wilddev.image.processors.ImageProcessor;
import com.wilddev.image.sdk.aop.LogTimeExecuted;

import java.net.URLConnection;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.wilddev.image.exceptions.files.*;
import com.wilddev.image.exceptions.*;
import com.wilddev.image.testers.*;
import java.io.*;
import java.util.Set;

@Service
public class ExternalImageService {

    private final MultipartProperties multipartProperties;

    private final FileService fileService;

    private final UrlService urlService;

    private final GridFsService gridFsService;

    private final MessageService messageService;

    private final ImageProcessor imageProcessor;

    private final ContentTypeTester contentTypeTester;

    public ExternalImageService(@Value("${image.allowed.types}") Set<ImageType> imageTypes,
                                MultipartProperties multipartProperties,
                                FileService fileService,
                                UrlService urlService,
                                GridFsService gridFsService,
                                MessageService messageService,
                                ImageProcessor imageProcessor) {

        this.contentTypeTester = new ImageContentTypeTester(imageTypes);

        this.multipartProperties = multipartProperties;
        this.fileService = fileService;
        this.urlService = urlService;
        this.gridFsService = gridFsService;
        this.messageService = messageService;
        this.imageProcessor = imageProcessor;
    }

    @LogTimeExecuted("ExternalImageService#download(image)")
    public void download(@NonNull Image image) throws ImageDownloadingException {

        final long maxFileSize = multipartProperties.getMaxFileSize().toBytes();

        try {
            URLConnection conn = urlService.getConnection(image.getSourceUrl());

            long contentLength = conn.getContentLengthLong();
            String contentType = conn.getContentType();

            if (contentLength > maxFileSize)
                throw new FileSizeLimitExceededException(messageService
                        .getMessage("exception.file.size.limit.exceeded", contentLength, maxFileSize));

            if (!contentTypeTester.test(contentType))
                throw new UnsupportedContentTypeException(messageService
                        .getMessage("exception.content.type.unsupported", contentType));

            File tempFile = fileService.createTempFile();
            image.setContentType(conn.getContentType());

            try (InputStream is = new BufferedInputStream(conn.getInputStream());
                 OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile))) {

                StreamUtils.copy(is, os);

                gridFsService.store(image.getId().toString(), image.getContentType(), tempFile);
                imageProcessor.process(ImageIO.read(tempFile), image);

            } finally {
                fileService.delete(tempFile);
            }

        } catch (Exception ex) {
            throw new ImageDownloadingException(messageService
                    .getMessage("exception.image.downloading.failed", image.getId()), ex);
        }
    }
}

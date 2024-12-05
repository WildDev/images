package fun.wilddev.images.handlers;

import fun.wilddev.images.models.ExternalImage;
import fun.wilddev.images.services.FileService;
import fun.wilddev.images.services.downloaders.FileDownloader;
import fun.wilddev.images.services.downloaders.callbacks.ImageReadyCallback;
import fun.wilddev.images.testers.ImageContentTypeExceptionalTester;
import fun.wilddev.spring.core.services.MessageService;

import java.net.URLConnection;

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import fun.wilddev.images.exceptions.*;
import fun.wilddev.images.exceptions.files.*;
import fun.wilddev.images.services.*;

@Service
public class ImageDownloadingHandler implements Handler<ExternalImage> {

    private final long maxFileSize;

    private final UrlService urlService;

    private final MessageService messageService;

    private final ImageContentTypeExceptionalTester contentTypeTester;

    private final FileDownloader fileDownloader;

    public ImageDownloadingHandler(MultipartProperties multipartProperties,
                                   UrlService urlService,
                                   FileService fileService,
                                   GridFsService gridFsService,
                                   MessageService messageService,
                                   ImageContentTypeExceptionalTester contentTypeTester,
                                   ImageReadyCallback imageReadyCallback) {

        this.fileDownloader = new FileDownloader(fileService, gridFsService, messageService, imageReadyCallback);

        this.maxFileSize = multipartProperties.getMaxFileSize().toBytes();
        this.urlService = urlService;
        this.messageService = messageService;
        this.contentTypeTester = contentTypeTester;
    }

    @Override
    public void handle(@NonNull ExternalImage image) throws RemoteConnectionException,
            UnsupportedContentTypeException, FileException {

        URLConnection conn = urlService.getConnection(image.sourceUrl());
        long contentLength = conn.getContentLengthLong();

        contentTypeTester.test(conn.getContentType());

        if (contentLength <= 0)
            throw new FileEmptyException(messageService.getMessage("exception.file.empty"));

        if (contentLength > maxFileSize)
            throw new FileSizeLimitExceededException(messageService
                    .getMessage("exception.file.size.limit.exceeded", contentLength, maxFileSize));

        fileDownloader.download(conn, image.id());
    }
}

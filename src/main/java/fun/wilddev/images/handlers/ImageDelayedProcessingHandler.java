package fun.wilddev.images.handlers;

import fun.wilddev.images.exceptions.gridfs.GridFsMissingFileException;
import fun.wilddev.images.exceptions.images.buffered.BufferedImageReadingException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.processors.ImageProcessor;
import fun.wilddev.spring.core.services.MessageService;

import lombok.AllArgsConstructor;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import fun.wilddev.images.services.*;

@AllArgsConstructor
@Component
public class ImageDelayedProcessingHandler implements Handler<FileMeta> {

    private final GridFsService gridFsService;

    private final ImageProcessor imageProcessor;

    private final BufferedImageReader imageReader;

    private final MessageService messageService;

    @Override
    public void handle(@NonNull FileMeta fileMeta) throws GridFsMissingFileException, BufferedImageReadingException {

        final String id = fileMeta.id();
        GridFsResource resource = gridFsService.findById(id);

        if (resource.exists()) {

            imageProcessor.process(imageReader.read(resource), fileMeta);
            return;
        }

        throw new GridFsMissingFileException(messageService.getMessage("exception.file.gridfs.not.found", id));
    }
}

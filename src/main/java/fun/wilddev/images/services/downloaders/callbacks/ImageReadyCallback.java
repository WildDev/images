package fun.wilddev.images.services.downloaders.callbacks;

import fun.wilddev.images.exceptions.images.buffered.BufferedImageReadingException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.processors.ImageProcessor;
import fun.wilddev.images.services.BufferedImageReader;

import java.io.File;

import lombok.AllArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ImageReadyCallback implements FileReadyCallback {

    private final ImageProcessor imageProcessor;

    private final BufferedImageReader bufferedImageReader;

    @Override
    public void onSuccess(@NonNull File file, @NonNull FileMeta meta) throws BufferedImageReadingException {
        imageProcessor.process(bufferedImageReader.read(file), meta);
    }
}

package fun.wilddev.images.services;

import fun.wilddev.images.exceptions.images.buffered.BufferedImageReadingException;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class BufferedImageReader {

    public BufferedImage read(@NonNull GridFsResource resource) throws BufferedImageReadingException {

        try (InputStream is = resource.getInputStream()) {
            return ImageIO.read(is);
        } catch (Exception ex) {
            throw new BufferedImageReadingException(ex);
        }
    }

    public BufferedImage read(@NonNull File resource) throws BufferedImageReadingException {

        try {
            return ImageIO.read(resource);
        } catch (Exception ex) {
            throw new BufferedImageReadingException(ex);
        }
    }
}

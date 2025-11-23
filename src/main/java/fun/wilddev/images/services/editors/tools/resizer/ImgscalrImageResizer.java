package fun.wilddev.images.services.editors.tools.resizer;

import java.awt.image.BufferedImage;

import org.imgscalr.Scalr;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class ImgscalrImageResizer implements ImageResizer {

    @Override
    public BufferedImage resize(@NonNull BufferedImage source, @NonNull Dimension target) {
        return Scalr.resize(source, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC,
                (int) target.getWidth(), (int) target.getHeight());
    }
}

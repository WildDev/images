package fun.wilddev.images.services.editors.tools.resizer;

import org.springframework.lang.NonNull;
import java.awt.image.BufferedImage;

import java.awt.*;

public interface ImageResizer {

    BufferedImage resize(@NonNull BufferedImage source, @NonNull Dimension target);
}

package fun.wilddev.images.processors.tasks;

import fun.wilddev.images.models.FileMeta;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public record SourceImage(BufferedImage bufferedImage, FileMeta fileMeta, Dimension dimension) {

}

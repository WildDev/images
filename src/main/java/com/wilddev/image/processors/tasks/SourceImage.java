package com.wilddev.image.processors.tasks;

import com.wilddev.image.entities.Image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public record SourceImage(BufferedImage bufferedImage, Image image, Dimension dimension) {

}

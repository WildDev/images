package com.wilddev.image.schedulers.feed;

import com.wilddev.image.entities.Image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wilddev.image.services.*;

@Service
public class ImagesFeed extends AbstractNewItemsFeed<Image> {

    public ImagesFeed(@Value("${image.feed.batch-size}") Integer batchSize,
                      ImageService imageService) {
        super(batchSize, imageService);
    }
}

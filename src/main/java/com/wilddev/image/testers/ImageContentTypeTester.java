package com.wilddev.image.testers;

import com.wilddev.image.enums.ImageType;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class ImageContentTypeTester implements ContentTypeTester {

    private final Set<String> imageTypes;

    public ImageContentTypeTester(Set<ImageType> imageTypes) {
        this.imageTypes = toContentTypes(imageTypes);
    }

    private Set<String> toContentTypes(Set<ImageType> imageTypes) {

        if (CollectionUtils.isEmpty(imageTypes)) {

            log.debug("No image types provided, skipping ...");
            return Collections.emptySet();
        }

        return imageTypes.stream().map(e ->
                "image/" + e.name().toLowerCase()).collect(Collectors.toSet());
    }

    @Override
    public boolean test(@NonNull String contentType) {
        return imageTypes.stream().anyMatch(e -> e.equalsIgnoreCase(contentType));
    }
}

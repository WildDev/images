package fun.wilddev.images.testers;

import fun.wilddev.images.enums.ImageType;
import fun.wilddev.images.testers.predefined.BooleanTester;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class ContentTypeTester implements BooleanTester<String> {

    private final Set<String> imageTypes;

    public ContentTypeTester(Set<ImageType> imageTypes) {
        this.imageTypes = toContentTypes(imageTypes);
    }

    private Set<String> toContentTypes(Set<ImageType> imageTypes) {

        if (CollectionUtils.isEmpty(imageTypes)) {

            log.debug("No image types provided, skipping ...");
            return Collections.emptySet();
        }

        return imageTypes.stream().map(e -> "image/" + e.name().toLowerCase()).collect(Collectors.toSet());
    }

    @Override
    public boolean test(String contentType) {
        return contentType != null && imageTypes.stream().anyMatch(e -> e.equalsIgnoreCase(contentType));
    }
}

package fun.wilddev.images.processors;

import fun.wilddev.images.entities.Image;
import fun.wilddev.images.queues.ImageRemovalQueue;
import fun.wilddev.images.repositories.ImageRepository;

import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@AllArgsConstructor
@Component
public class ImageRemovalProcessor {

    private final ImageRemovalQueue imageRemovalQueue;

    private final ImageRepository imageRepository;

    private Set<String> extractIds(List<Image> source) {
        return source.stream().map(Image::getId).collect(Collectors.toSet());
    }

    public void process(@NonNull String id) {

        Set<String> ids = new HashSet<>();

        ids.add(id);
        ids.addAll(extractIds(imageRepository.findBySourceId(id)));

        ids.forEach(i -> {

            imageRemovalQueue.push(i);
            log.debug("Image {} is queued to delete", i);
        });
    }
}

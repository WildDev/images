package fun.wilddev.images.services;

import fun.wilddev.images.config.props.ImageProcessorProps;
import fun.wilddev.images.models.FileMeta;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import fun.wilddev.images.enums.*;
import fun.wilddev.images.processors.tasks.*;
import java.util.*;

@Slf4j
@Service
public class ImageTaskRunner {

    private final Set<ImageType> editableImageTypes;

    private final List<ImagePostProcessingTask> tasks;

    private final ImageService imageService;

    public ImageTaskRunner(ImageProcessorProps imageProcessorProps,
                           ImageService imageService,
                           Map<String, ImagePostProcessingTask> taskMap) {

        this.editableImageTypes = imageProcessorProps.editableTypes();
        this.tasks = filterTasks(imageProcessorProps.tasks(), taskMap);

        this.imageService = imageService;
    }

    private List<ImagePostProcessingTask> filterTasks(Set<ImageTaskType> tasks,
                                                      Map<String, ImagePostProcessingTask> taskMap) {

        return tasks.stream().filter(task -> taskMap.containsKey(task.name()))
                .map(task -> taskMap.get(task.name()))
                .collect(Collectors.toList());
    }

    private boolean isEditable(FileMeta fileMeta) {
        return editableImageTypes.contains(imageService.getImageType(fileMeta.contentType()));
    }

    public void runTasks(@NonNull BufferedImage bufferedImage, @NonNull FileMeta fileMeta) {

        if (tasks.isEmpty()) {

            log.debug("No post processing tasks, skipping ...");
            return;
        }

        if (!isEditable(fileMeta)) {

            log.debug("Uneditable image type, skipping ...");
            return;
        }

        SourceImage source = new SourceImage(bufferedImage, fileMeta,
                new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

        tasks.forEach(task -> task.run(source));
        log.debug("{} tasks processed for image {}", tasks.size(), fileMeta.id());
    }
}

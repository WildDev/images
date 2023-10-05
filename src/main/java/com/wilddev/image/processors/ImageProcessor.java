package com.wilddev.image.processors;

import com.wilddev.image.entities.Image;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.services.ImageService;
import com.wilddev.image.services.webhooks.ImageWebhookService;

import java.awt.image.BufferedImage;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.wilddev.image.enums.*;
import com.wilddev.image.processors.tasks.*;
import java.awt.*;
import java.util.stream.*;
import java.util.*;

@Slf4j
@Component
public class ImageProcessor {

    private final Set<ImageType> editableImageTypes;

    private final ImageService imageService;

    private final ImageWebhookService imageWebhookService;

    private final List<ImagePostProcessingTask> tasks;

    public ImageProcessor(@Value("${image.processor.tasks}") Set<ImageTaskType> tasks,
                          @Value("${image.processor.editable.types}") Set<ImageType> editableImageTypes,
                          ImageService imageService,
                          ImageWebhookService imageWebhookService,
                          Map<String, ImagePostProcessingTask> taskMap) {

        this.editableImageTypes = editableImageTypes;
        this.imageService = imageService;
        this.imageWebhookService = imageWebhookService;
        this.tasks = filterTasks(tasks, taskMap);
    }

    private List<ImagePostProcessingTask> filterTasks(Set<ImageTaskType> tasks,
                                                      Map<String, ImagePostProcessingTask> taskMap) {

        return tasks.stream().filter(task -> taskMap.containsKey(task.name()))
                .map(task -> taskMap.get(task.name()))
                .collect(Collectors.toList());
    }

    private void setDimension(BufferedImage bufferedImage, Image target) {

        target.setWidth(bufferedImage.getWidth());
        target.setHeight(bufferedImage.getHeight());
    }

    private boolean isEditable(Image image) {
        return editableImageTypes.contains(imageService.getImageType(image.getContentType()));
    }

    private void runTasks(BufferedImage bufferedImage, Image target) {

        if (tasks.isEmpty()) {

            log.debug("No post processing tasks, skipping ...");
            return;
        }

        if (!isEditable(target)) {

            log.debug("Uneditable image type, skipping ...");
            return;
        }

        SourceImage source = new SourceImage(bufferedImage, target,
                new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));

        tasks.forEach(task -> task.run(source));
        log.debug("{} tasks processed for image {}", tasks.size(), target.getId());
    }

    @LogTimeExecuted("ImageProcessor#process(bufferedImage, target)")
    public void process(@NonNull BufferedImage bufferedImage, @NonNull Image target) {

        try {
            setDimension(bufferedImage, target);
            runTasks(bufferedImage, target);

            imageService.setProcessed(target);
            imageWebhookService.sendImageReady(target);

        } catch (Exception ex) {

            log.error("Failed to process image {}", target.getId(), ex);

            imageService.setFailed(target);
            imageWebhookService.sendImageFailed(target);
        }
    }
}

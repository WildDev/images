package com.wilddev.image.services;

import com.wilddev.image.entities.Image;
import com.wilddev.image.repositories.ImageRepository;
import com.wilddev.image.sdk.aop.LogTimeExecuted;
import com.wilddev.image.sdk.models.ExpiredRecordsCollector;

import java.awt.Dimension;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.wilddev.image.enums.*;
import com.wilddev.image.exceptions.files.*;
import com.wilddev.image.exceptions.images.*;
import com.wilddev.image.exceptions.*;
import com.wilddev.image.testers.*;
import java.util.*;
import java.util.regex.*;
import org.springframework.data.domain.*;

@Slf4j
@Service
public class ImageService implements ReservationService<Image>, ExpiredRecordsCollector {

    private final static Pattern IMAGE_PATTERN = Pattern.compile("^image/([a-z]+)$");

    private final static int FORMAT_NAME_GROUP = 1;

    private final Integer timeout;

    private final GridFsService gridFsService;

    private final ExpirationDateService expirationDateService;

    private final MessageService messageService;

    private final ContentTypeTester contentTypeTester;

    private final ImageRepository imageRepository;

    public ImageService(@Value("${image.allowed.types}") Set<ImageType> imageTypes,
                        @Value("${image.timeout}") Integer timeout,
                        GridFsService gridFsService,
                        ExpirationDateService expirationDateService,
                        MessageService messageService,
                        ImageRepository imageRepository) {

        this.contentTypeTester = new ImageContentTypeTester(imageTypes);

        this.timeout = timeout;
        this.gridFsService = gridFsService;
        this.expirationDateService = expirationDateService;
        this.messageService = messageService;
        this.imageRepository = imageRepository;
    }

    @LogTimeExecuted("ImageService#add(sourceUrl, multiSize)")
    @Transactional
    public Image add(@NonNull String sourceUrl, @NonNull Boolean multiSize) {

        LocalDateTime now = LocalDateTime.now();

        return imageRepository.save(new Image(sourceUrl, ImageStatus.NEW, multiSize,
                now, expirationDateService.calc(now, timeout)));
    }

    @LogTimeExecuted("ImageService#add(source, dimension)")
    @Transactional
    public Image add(@NonNull Image source, @NonNull Dimension dimension) {

        LocalDateTime now = LocalDateTime.now();

        return imageRepository.save(new Image(source, dimension, ImageStatus.NEW,
                false, now, expirationDateService.calc(now, timeout)));
    }

    @LogTimeExecuted("ImageService#upload(file, multiSize)")
    @Transactional
    public ObjectId upload(@NonNull MultipartFile file, @NonNull Boolean multiSize) throws FileException {

        final String contentType = file.getContentType();

        try {
            if (contentType == null || !contentTypeTester.test(contentType))
                throw new UnsupportedContentTypeException(messageService
                        .getMessage("exception.content.type.unsupported", contentType));

            LocalDateTime now = LocalDateTime.now();

            Image image = new Image(ImageStatus.NEW, contentType, multiSize,
                    now, expirationDateService.calc(now, timeout));

            imageRepository.save(image);
            gridFsService.store(image.getId().toString(), contentType, file.getInputStream());

            return image.getId();

        } catch (Exception ex) {
            throw new ImageUploadingException(messageService
                    .getMessage("exception.image.uploading.failed"), ex);
        }
    }

    @LogTimeExecuted("ImageService#findProcessed(id)")
    @Transactional
    public Optional<Image> findProcessed(@NonNull String id) {
        return imageRepository.findByIdAndStatus(id, ImageStatus.PROCESSED);
    }

    @LogTimeExecuted("ImageService#findProcessed(sourceId, width, height)")
    @Transactional
    public Optional<Image> findProcessed(@NonNull String sourceId, @NonNull Integer width, @NonNull Integer height) {
        return imageRepository.findBySourceIdAndStatusAndWidthAndHeight(sourceId,
                ImageStatus.PROCESSED, width, height);
    }

    @LogTimeExecuted("ImageService#findProcessedByWidth(sourceId, width)")
    @Transactional
    public List<Image> findProcessedByWidth(@NonNull String sourceId, @NonNull Integer width) {
        return imageRepository.findBySourceIdAndStatusAndWidth(sourceId, ImageStatus.PROCESSED, width);
    }

    @LogTimeExecuted("ImageService#findProcessedByHeight(sourceId, height)")
    @Transactional
    public List<Image> findProcessedByHeight(@NonNull String sourceId, @NonNull Integer height) {
        return imageRepository.findBySourceIdAndStatusAndHeight(sourceId, ImageStatus.PROCESSED, height);
    }

    @LogTimeExecuted("ImageService#listNew(batchSize)")
    @Transactional
    @Override
    public List<Image> listNew(int batchSize) {
        return imageRepository.findByStatus(ImageStatus.NEW,
                PageRequest.of(0, batchSize, Sort.Direction.ASC, "sourceUrl", "timestamp"));
    }

    @LogTimeExecuted("ImageService#reserve(images)")
    @Transactional
    @Override
    public void reserve(@NonNull List<Image> images) {

        images.forEach(image -> image.setStatus(ImageStatus.IN_PROGRESS));
        imageRepository.saveAll(images);
    }

    @LogTimeExecuted("ImageService#setProcessed(image)")
    @Transactional
    public void setProcessed(@NonNull Image image) {

        image.setStatus(ImageStatus.PROCESSED);
        image.setProcessed(LocalDateTime.now());

        imageRepository.save(image);

        log.info("Image {} is processed", image.getId());
    }

    @LogTimeExecuted("ImageService#setFailed(image)")
    @Transactional
    public void setFailed(@NonNull Image image) {

        image.setStatus(ImageStatus.FAILED);
        imageRepository.save(image);

        log.info("Image {} is failed", image.getId());
    }

    @LogTimeExecuted("ImageService#getFormatName(contentType)")
    public String getFormatName(@NonNull String contentType) throws ImageFormatException {

        if (StringUtils.hasText(contentType)) {

            Matcher matcher = IMAGE_PATTERN.matcher(contentType);

            if (matcher.matches())
                return matcher.group(FORMAT_NAME_GROUP);
        }

        throw new ImageFormatException(messageService
                .getMessage("exception.image.unknown.content.type", contentType));
    }

    @LogTimeExecuted("ImageService#getImageType(contentType)")
    public ImageType getImageType(@NonNull String contentType) {

        try {
            return ImageType.valueOf(getFormatName(contentType).toUpperCase());
        } catch (Exception ex) {

            log.warn("Failed to get the image type for {}", contentType, ex);
            return null;
        }
    }

    @LogTimeExecuted("ImageService#collectExpired")
    @Override
    public long collectExpired() {
        return imageRepository.findAndSetExpiredByStatusAndExpiresBefore(ImageStatus.NEW,
                LocalDateTime.now(), ImageStatus.EXPIRED);
    }
}

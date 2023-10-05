package fun.wilddev.images.services;

import fun.wilddev.images.config.props.ImageProps;
import fun.wilddev.images.entities.Image;
import fun.wilddev.images.exceptions.files.FileException;
import fun.wilddev.images.models.FileMeta;
import fun.wilddev.images.repositories.ImageRepository;
import fun.wilddev.images.testers.ImageContentTypeExceptionalTester;
import fun.wilddev.spring.core.services.MessageService;
import fun.wilddev.spring.core.services.date.FutureCalculator;

import java.awt.Dimension;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import fun.wilddev.images.enums.*;
import fun.wilddev.images.exceptions.images.*;
import java.util.*;
import java.util.regex.*;
import org.springframework.data.domain.*;

@Slf4j
@Service
public class ImageService implements Slicer<Image> {

    private final static Pattern IMAGE_PATTERN = Pattern.compile("^image/([a-z]+)$");

    private final static int FORMAT_NAME_GROUP = 1;

    private final String timeout;

    private final GridFsService gridFsService;

    private final FutureCalculator futureCalculator;

    private final MessageService messageService;

    private final ImageContentTypeExceptionalTester contentTypeTester;

    private final ImageRepository imageRepository;

    public ImageService(ImageProps imageProps,
                        GridFsService gridFsService,
                        FutureCalculator futureCalculator,
                        MessageService messageService,
                        ImageContentTypeExceptionalTester contentTypeTester,
                        ImageRepository imageRepository) {

        this.timeout = imageProps.timeout();

        this.contentTypeTester = contentTypeTester;
        this.gridFsService = gridFsService;
        this.futureCalculator = futureCalculator;
        this.messageService = messageService;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public Image add(@NonNull String sourceUrl, @NonNull Boolean multiSize) {

        LocalDateTime now = LocalDateTime.now();

        return imageRepository.save(new Image(sourceUrl, ImageStatus.NEW, multiSize,
                now, futureCalculator.calc(now, timeout)));
    }

    @Transactional
    public Image add(@NonNull FileMeta fileMeta, @NonNull Dimension dimension) {

        LocalDateTime now = LocalDateTime.now();

        return imageRepository.save(new Image(fileMeta, dimension, ImageStatus.NEW,
                false, now, futureCalculator.calc(now, timeout)));
    }

    @Transactional
    public String upload(@NonNull MultipartFile file, @NonNull Boolean multiSize) throws FileException {

        final String contentType = file.getContentType();

        try {
            contentTypeTester.test(contentType);

            LocalDateTime now = LocalDateTime.now();

            Image image = new Image(ImageStatus.NEW, contentType, multiSize,
                    now, futureCalculator.calc(now, timeout));

            imageRepository.save(image);
            gridFsService.store(image.getId(), contentType, file.getInputStream());

            return image.getId();

        } catch (Exception ex) {
            throw new ImageUploadingException(messageService
                    .getMessage("exception.image.uploading.failed"), ex);
        }
    }

    @Transactional
    public Optional<Image> findProcessed(@NonNull String id) {
        return imageRepository.findByIdAndStatus(id, ImageStatus.PROCESSED);
    }

    @Transactional
    public Optional<Image> findProcessed(@NonNull String sourceId, @NonNull Integer width, @NonNull Integer height) {
        return imageRepository.findBySourceIdAndStatusAndWidthAndHeight(sourceId,
                ImageStatus.PROCESSED, width, height);
    }

    @Transactional
    public List<Image> findProcessedByWidth(@NonNull String sourceId, @NonNull Integer width) {
        return imageRepository.findBySourceIdAndStatusAndWidth(sourceId, ImageStatus.PROCESSED, width);
    }

    @Transactional
    public List<Image> findProcessedByHeight(@NonNull String sourceId, @NonNull Integer height) {
        return imageRepository.findBySourceIdAndStatusAndHeight(sourceId, ImageStatus.PROCESSED, height);
    }

    @Transactional
    @Override
    public Slice<Image> sliceNew(int size) {
        return imageRepository.findByStatus(ImageStatus.NEW,
                PageRequest.of(0, size, Sort.Direction.ASC, "added"));
    }

    @Transactional
    public void setQueued(@NonNull String id) {
        imageRepository.findAndSetStatusById(id, ImageStatus.QUEUED);
    }

    @Transactional
    public void setProcessed(@NonNull String id) {

        imageRepository.findAndSetStatusAndProcessedById(id, ImageStatus.PROCESSED, LocalDateTime.now());
        log.info("Image {} is processed", id);
    }

    @Transactional
    public void setFailed(@NonNull String id) {

        imageRepository.findAndSetStatusById(id, ImageStatus.FAILED);
        log.info("Image {} is failed", id);
    }

    public String getFormatName(@NonNull String contentType) throws ImageFormatException {

        if (StringUtils.hasText(contentType)) {

            Matcher matcher = IMAGE_PATTERN.matcher(contentType);

            if (matcher.matches())
                return matcher.group(FORMAT_NAME_GROUP);
        }

        throw new ImageFormatException(messageService
                .getMessage("exception.image.unknown.content.type", contentType));
    }

    public ImageType getImageType(@NonNull String contentType) {

        try {
            return ImageType.valueOf(getFormatName(contentType).toUpperCase());
        } catch (Exception ex) {

            log.warn("Failed to get the image type for {}", contentType, ex);
            return null;
        }
    }

    @Transactional
    public void collectExpired(@NonNull LocalDateTime bound) {

        log.info("{} records were expired", imageRepository
                .findAndSetExpiredByStatusAndExpiresBefore(ImageStatus.NEW, bound, ImageStatus.EXPIRED));
    }
}

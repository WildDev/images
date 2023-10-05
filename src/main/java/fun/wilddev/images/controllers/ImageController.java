package fun.wilddev.images.controllers;

import fun.wilddev.images.controllers.responses.ID;
import fun.wilddev.images.validators.ImageUploadRequestValidator;
import fun.wilddev.spring.web.controllers.AbstractController;
import fun.wilddev.spring.web.mappers.MultiValueMapper;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.validation.Errors;

import fun.wilddev.images.controllers.requests.*;
import fun.wilddev.images.services.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/images")
@RestController
public class ImageController extends AbstractController {

    private final ImageUploadRequestValidator imageUploadRequestValidator;

    private final ImageService imageService;

    private final ImageLoadingService imageLoadingService;

    private final MultiValueMapper multiValueMapper;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody ExternalImageAddRequest request, Errors errors) {

        if (errors.hasErrors())
            return badRequest(errors);

        final String imageId = imageService.add(request.getUrl(), request.getMultiSize()).getId();
        log.info("External image is scheduled to download {}", imageId);

        return created(new ID<>(imageId));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@Valid ImageUploadRequest request, Errors errors) throws Exception {

        imageUploadRequestValidator.validate(request, errors);

        if (errors.hasErrors())
            return badRequest(errors);

        final String imageId = imageService.upload(request.getFile(), request.getMultiSize());
        log.info("New image uploaded {}", imageId);

        return created(new ID<>(imageId));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findOne(@PathVariable String id,
                                     @RequestParam(required = false) Integer width,
                                     @RequestParam(required = false) Integer height) {

        final GridFsResource resource = imageLoadingService.findOne(id, width, height);

        return resource == null ? notFound() : ok(resource, multiValueMapper
                .map(Map.of(HttpHeaders.CONTENT_TYPE, resource.getContentType())));
    }
}

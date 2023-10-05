package com.wilddev.image.repositories.settings;

import com.wilddev.image.entities.settings.ResizeSize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResizeSizeRepository extends MongoRepository<ResizeSize, String> {

}

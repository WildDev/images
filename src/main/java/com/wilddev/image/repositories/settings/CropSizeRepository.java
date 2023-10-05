package com.wilddev.image.repositories.settings;

import com.wilddev.image.entities.settings.CropSize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CropSizeRepository extends MongoRepository<CropSize, String> {

}

package fun.wilddev.images.repositories.settings;

import fun.wilddev.images.entities.settings.CropSize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CropSizeRepository extends MongoRepository<CropSize, String> {

}

package fun.wilddev.images.repositories.settings;

import fun.wilddev.images.entities.settings.ResizeSize;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResizeSizeRepository extends MongoRepository<ResizeSize, String> {

}

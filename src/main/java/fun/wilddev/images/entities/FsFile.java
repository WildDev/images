package fun.wilddev.images.entities;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document("fs.files")
@Setter
@Getter
@ToString
public class FsFile {

    @Indexed
    private String filename;
}

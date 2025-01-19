package fun.wilddev.images.entities;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.*;

@Document("fs.chunks")
@Setter
@Getter
@ToString
public class FsChunk {

    @Indexed
    @Field("files_id")
    private String filesId;
}

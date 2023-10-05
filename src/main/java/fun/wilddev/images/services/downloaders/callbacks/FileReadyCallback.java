package fun.wilddev.images.services.downloaders.callbacks;

import fun.wilddev.images.models.FileMeta;
import java.io.File;
import org.springframework.lang.NonNull;

public interface FileReadyCallback {

    void onSuccess(@NonNull File file, @NonNull FileMeta meta) throws Exception;
}

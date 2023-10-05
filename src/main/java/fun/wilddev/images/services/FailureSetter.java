package fun.wilddev.images.services;

import org.springframework.lang.NonNull;

public interface FailureSetter {

    void setFailed(@NonNull String id);
}

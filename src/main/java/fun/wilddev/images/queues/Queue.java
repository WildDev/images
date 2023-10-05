package fun.wilddev.images.queues;

import org.springframework.lang.NonNull;

public interface Queue<T> {

    void push(@NonNull T item) throws Exception;
}

package fun.wilddev.images.experimental.models;

public interface Task<T> {

    void run(T context);
}

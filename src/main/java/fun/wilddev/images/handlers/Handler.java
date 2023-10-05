package fun.wilddev.images.handlers;

public interface Handler<T> {

    void handle(T input) throws Exception;
}

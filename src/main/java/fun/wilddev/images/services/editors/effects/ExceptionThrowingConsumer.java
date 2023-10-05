package fun.wilddev.images.services.editors.effects;

@FunctionalInterface
public interface ExceptionThrowingConsumer<T, Y> {

    void accept(T p1, Y p2) throws Exception;
}

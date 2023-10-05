package fun.wilddev.images.testers.predefined;

public interface BooleanTester<T> extends ConditionTester {

    boolean test(T subject);
}

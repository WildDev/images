package fun.wilddev.images.testers.predefined;

public interface ExceptionalTester<T> extends ConditionTester {

    void test(T subject) throws Exception;
}

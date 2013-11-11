package concurrency.stm;

public abstract class Context {
    abstract <T> T get(Ref<T> ref);
}

package architect;

public interface Validator<T> {
    boolean isValid(T t);
}
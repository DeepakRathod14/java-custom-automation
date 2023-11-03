package framework.utilities.softasst;

public class WrappedObject<T> {

    private final Class<?> cls;
    private final T object;

    public WrappedObject(T wrappedObject) {
        this.object = wrappedObject;
        this.cls = wrappedObject.getClass();
    }

    public static <T> WrappedObject<T> wrap(T object) {
        return new WrappedObject<>(object);
    }

    public Class<?> getCls() {
        return cls;
    }

    public T getObject() {
        return object;
    }
}

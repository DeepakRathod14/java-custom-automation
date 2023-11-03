package framework.utilities.timestampbuilder;

import java.util.function.Consumer;

public final class ConsumerFieldSetter<T> implements FieldSetter<T> {
    private final T returnObject;
    private final Consumer<String> consumer;

    ConsumerFieldSetter(T returnObject, Consumer<String> consumer) {
        if (returnObject == null || consumer == null) {
            throw new IllegalArgumentException();
        }

        this.returnObject = returnObject;
        this.consumer = consumer;
    }

    @Override
    public T setFieldValue(String timestamp) {
        consumer.accept(timestamp);
        return returnObject;
    }
}
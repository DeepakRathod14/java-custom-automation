package framework.utilities.timestampbuilder;

import java.util.function.Function;

public final class FunctionFieldSetter<T> implements FieldSetter<T> {
    private final Function<String, T> function;

    FunctionFieldSetter(Function<String, T> function) {
        if (function == null) {
            throw new IllegalArgumentException();
        }

        this.function = function;
    }

    @Override
    public T setFieldValue(String timestamp) {
        return function.apply(timestamp);
    }
}

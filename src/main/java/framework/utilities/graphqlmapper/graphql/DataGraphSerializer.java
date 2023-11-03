package framework.utilities.graphqlmapper.graphql;

public interface DataGraphSerializer<T> {

    T serialize(T value);

    class None implements DataGraphSerializer<Object> {
        @Override
        public Object serialize(Object value) {
            return value;
        }
    }

}

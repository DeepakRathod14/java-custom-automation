package framework.utilities.timestampbuilder;

public class NullFieldSetter implements FieldSetter<String> {

    private static final NullFieldSetter INSTANCE = new NullFieldSetter();

    private NullFieldSetter() {
    }

    public static NullFieldSetter getInstance() {
        return INSTANCE;
    }

    @Override
    public String setFieldValue(String timestamp) {
        return timestamp;
    }
}

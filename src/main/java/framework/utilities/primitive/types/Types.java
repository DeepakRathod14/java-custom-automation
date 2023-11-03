package framework.utilities.primitive.types;

import java.util.Random;

public enum Types {

    INTEGER("int") {
        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }

        public Class<Integer> getType() {
            return Integer.class;
        }
    },
    LONG("long") {
        @Override
        public Long convert(String value) {
            return Long.valueOf(value);
        }

        public Class<Long> getType() {
            return Long.class;
        }
    },
    DOUBLE("double") {
        @Override
        public Double convert(String value) {
            return Double.valueOf(value);
        }

        public Class<Double> getType() {
            return Double.class;
        }
    },
    FLOAT("float") {
        @Override
        public Float convert(String value) {
            return Float.valueOf(value);
        }

        public Class<Float> getType() {
            return Float.class;
        }
    },
    STRING("string") {
        @Override
        public String convert(String value) {
            return value;
        }

        public Class<String> getType() {
            return String.class;
        }
    },
    BOOLEAN("bool") {
        @Override
        public Boolean convert(String value) {
            return Boolean.valueOf(value);
        }

        public Class<Boolean> getType() {
            return Boolean.class;
        }
    };

    private String name;

    Types(String name) {
        this.name = name;
    }

    /**
     * Return type from String.
     * @param text input string
     * @return Types
     */
    public static Types fromString(String text) {
        String locate = text.toLowerCase();
        for (Types b : Types.values()) {
            if (b.name.startsWith(locate)) {
                return b;
            }
        }
        throw new NullPointerException();
    }

    /**
     * Return Type from input object.
     * @param object input object
     * @return Types
     */
    public static Types fromObject(Object object) {
        if (object instanceof Integer) {
            return Types.INTEGER;
        }
        if (object instanceof Long) {
            return Types.LONG;
        }
        if (object instanceof Double) {
            return Types.DOUBLE;
        }
        if (object instanceof Float) {
            return Types.FLOAT;
        }
        if (object instanceof String) {
            return Types.STRING;
        }
        if (object instanceof Boolean) {
            return Types.BOOLEAN;
        }
        throw new NullPointerException();
    }

    /**
     * Return random type.
     * @return Types
     */
    public static Types random() {
        return Types.values()[new Random().nextInt(Types.values().length)];
    }

    public abstract <T> T getType();

    public abstract <T> T convert(String value);
    
}

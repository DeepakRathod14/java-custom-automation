package framework.utilities.primitive;

import framework.utilities.primitive.generators.BooleanGenerator;
import framework.utilities.primitive.generators.DoubleGenerator;
import framework.utilities.primitive.generators.FloatGenerator;
import framework.utilities.primitive.generators.IPrimitiveGenerator;
import framework.utilities.primitive.generators.IntegerGenerator;
import framework.utilities.primitive.generators.LongGenerator;
import framework.utilities.primitive.generators.StringGenerator;
import framework.utilities.primitive.types.Types;

public class RandomValueGeneratorProvider {

    private Object valueToProcess;
    private String typeString;
    private Class<?> type;

    public RandomValueGeneratorProvider(Object value) {
        initFields(value, null, null);
    }

    public RandomValueGeneratorProvider(Class<?> type) {
        initFields(null, type, null);
    }

    public RandomValueGeneratorProvider() {
    }

    public RandomValueGeneratorProvider fromObject(Object value) {
        initFields(value, null, null);
        return this;
    }

    public RandomValueGeneratorProvider fromCls(Class<?> cls) {
        initFields(null, cls, null);
        return this;
    }

    public RandomValueGeneratorProvider fromString(String typeName) {
        initFields(null, null, typeName);
        return this;
    }

    private void initFields(Object valueToProcess, Class<?> type, String typeName) {
        this.valueToProcess = valueToProcess;
        this.type = type;
        this.typeString = typeName;
    }

    /**
     * Provides generator based on inputs.
     * @return IPrimitiveGenerator implementation depends on a type
     */
    public IPrimitiveGenerator provideGenerator() {
        if (this.valueToProcess != null) {
            return processObject();
        } else if (this.typeString != null) {
            return processString();
        } else if (this.type != null) {
            return processType();
        }
        throw new UnknownTypeProvidedException("Provide object or type");
    }

    private IPrimitiveGenerator processObject() {
        return getGenerator(Types.fromObject(this.valueToProcess).getType());
    }

    private IPrimitiveGenerator processString() {
        return getGenerator(Types.fromString(this.typeString).getType());
    }

    private IPrimitiveGenerator processType() {
        return getGenerator(this.type);
    }

    private IPrimitiveGenerator getGenerator(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return new IntegerGenerator();
        }
        if (type == long.class || type == Long.class) {
            return new LongGenerator();
        }
        if (type == double.class || type == Double.class) {
            return new DoubleGenerator();
        }
        if (type == float.class || type == Float.class) {
            return new FloatGenerator();
        }
        if (type == String.class) {
            return new StringGenerator();
        }
        if (type == boolean.class || type == Boolean.class) {
            return new BooleanGenerator();
        }
        throw new UnknownTypeProvidedException("Primitive type is supported");
    }

}

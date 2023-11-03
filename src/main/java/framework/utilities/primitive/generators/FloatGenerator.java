package framework.utilities.primitive.generators;

import java.util.Random;

import framework.utilities.random.RandomHelper;

public class FloatGenerator implements IPrimitiveGenerator<Float, Float> {

    @Override
    public Float generate() {
        Float max = Float.MAX_VALUE;
        Float min = 1.0f;
        return generate(min, max);
    }

    @Override
    public Float generate(Float min, Float max) {
        Random rand = RandomHelper.random();
        return rand.nextFloat() * (max - min) + min;
    }

}

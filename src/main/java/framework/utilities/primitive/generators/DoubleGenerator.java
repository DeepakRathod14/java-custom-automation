package framework.utilities.primitive.generators;

import java.util.Random;

import framework.utilities.random.RandomHelper;

public class DoubleGenerator implements IPrimitiveGenerator<Double, Double> {

    @Override
    public Double generate() {
        Double maxX = Double.MAX_VALUE;
        Double minX = 1.0;
        return generate(minX, maxX);
    }

    @Override
    public Double generate(Double min, Double max) {
        Random rand = RandomHelper.random();
        return rand.nextDouble() * (max - min) + min;
    }

}

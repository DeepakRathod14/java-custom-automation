package framework.utilities.primitive.generators;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class IntegerGenerator implements IPrimitiveGenerator<Integer, Integer> {


    @Override
    public Integer generate() {
        Integer max = Integer.MAX_VALUE;
        Integer min = 1;
        return generate(min, max);
    }

    @Override
    public Integer generate(Integer min, Integer max) {
        if (Objects.equals(min, max) && !max.equals(Integer.MAX_VALUE)) {
            max += 1;
        }
        return ThreadLocalRandom.current().nextInt(min, max);
    }

}

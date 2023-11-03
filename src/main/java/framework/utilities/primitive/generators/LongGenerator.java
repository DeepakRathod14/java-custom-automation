package framework.utilities.primitive.generators;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class LongGenerator implements IPrimitiveGenerator<Long, Long> {

    @Override
    public Long generate() {
        Long min = 1L;
        Long max = Long.MAX_VALUE;
        return generate(min, max);
    }

    @Override
    public Long generate(Long min, Long max) {
        if (Objects.equals(min, max) && !max.equals(Long.MAX_VALUE)) {
            max += 1;
        }
        return ThreadLocalRandom.current().nextLong(min, max);
    }

}

package framework.utilities.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class RandomHelper {

    private static final AtomicLong SEED_INCREMENT = new AtomicLong(0L);

    private RandomHelper() {
        //default private
    }

    public static Random random() {
        return new Random(System.currentTimeMillis() + SEED_INCREMENT.getAndIncrement());
    }

}

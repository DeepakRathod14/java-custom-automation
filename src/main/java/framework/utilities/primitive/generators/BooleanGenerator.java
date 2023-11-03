package framework.utilities.primitive.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class BooleanGenerator implements IPrimitiveGenerator<Boolean, Boolean> {

    @Override
    public Boolean generate() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @Override
    public Boolean generate(Boolean min, Boolean max) {
        return Objects.equals(min, max) ? max : generate();
    }

    @Override
    public List<Boolean> generateUniqueList(int size, Boolean min, Boolean max) {
        List<Boolean> tlist = new ArrayList<>();
        tlist.add(true);
        if (size > 2) {
            size = 2;
        }
        while (tlist.size() < size) {
            Boolean randomValue = generate(min, max);
            if (!tlist.contains(randomValue)) {
                tlist.add(randomValue);
            }
        }
        Collections.sort(tlist);
        return tlist;
    }
}

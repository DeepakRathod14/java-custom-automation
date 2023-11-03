package framework.utilities.primitive.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IPrimitiveGenerator<T extends Comparable<T>, S> {

    /**
     * Generate random primitive.
     * @return T
     */
    T generate();

    /**
     * Generate random primitive considering min and max.
     * @param min - min value
     * @param max - max value
     * @return T
     */
    T generate(S min, S max);

    /**
     * Generate unique list of random values.
     * @param size preferred list size
     * @return List
     */
    default List<T> generateList(int size) {
        List<T> tlist = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tlist.add(generate());
        }
        return tlist;
    }

    /**
     * Generate list of random values.
     * @param size preferred list size
     * @param min min value
     * @param max max value
     * @return List
     */
    default List<T> generateList(int size, S min, S max) {
        List<T> tlist = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            tlist.add(generate(min, max));
        }
        Collections.sort(tlist);
        return tlist;
    }

    /**
     * Generate unique list of random values.
     * @param size preferred list size
     * @param min min value
     * @param max max value
     * @return List
     */
    default List<T> generateUniqueList(int size, S min, S max) {
        List<T> tlist = new ArrayList<>();
        while (tlist.size() < size) {
            T randomValue = generate(min, max);
            if (!tlist.contains(randomValue)) {
                tlist.add(randomValue);
            }
        }
        Collections.sort(tlist);
        return tlist;
    }

}

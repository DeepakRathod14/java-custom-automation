package framework.utilities;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public final class CollectionUtil {

    private CollectionUtil() {
        //default empty constructor
    }

    /**
     * Find and remove first occurrence in iterable.
     *
     * @param collection Iterable{@literal <}? extends T{@literal >}
     * @param test       Predicate{@literal <}? super T{@literal >}
     * @param <T>        T
     * @return {@literal <}T{@literal >} T - located value
     */
    public static <T> T findAndRemoveFirst(Iterable<? extends T> collection, Predicate<? super T> test) {
        for (Iterator<? extends T> it = collection.iterator(); it.hasNext(); ) {
            T value = it.next();
            if (test.test(value)) {
                it.remove();
                return value;
            }
        }
        return null;
    }

    /**
     * Retrieve string representation of minimum number from input list.
     *
     * @param list list containing numbers
     * @return string representation of min value in the list
     */
    public static String getMinNumberFromList(List<? extends Number> list) {
        Optional<? extends Number> optional = list.stream()
            .min(getComparator(list));
        if (optional.isPresent()) {
            return optional.get()
                .toString();
        } else {
            return "0";
        }
    }

    /**
     * Retrieve string representation of maximum number from input list.
     *
     * @param list list containing numbers
     * @return string representation of min value in the list
     */
    public static String getMaxNumberFromList(List<? extends Number> list) {
        Optional<? extends Number> optional = list.stream()
            .max(getComparator(list));
        if (optional.isPresent()) {
            return optional.get()
                .toString();
        } else {
            return "0";
        }
    }

    private static Comparator<? super Number> getComparator(List<? extends Number> list) {
        Number comparingNumber = list.get(0);
        if (comparingNumber instanceof Double || comparingNumber instanceof Float) {
            return Comparator.comparing(Number::doubleValue);
        } else {
            return Comparator.comparing(Number::longValue);
        }
    }

    /**
     * Compare all elements using defined elements
     * The same as you would check nulls and sizes and then list.containsAll(anotherList) && anotherList.containsAll
     *
     * @param biPredicate - a predicate how to test if lists are equal. i.e (x,y) -> x.getName().equals(y.getName())
     * @return boolean value indicating that lists are equal by given predicate or not
     */
    public static <T, R> boolean compareAllByPredicate(List<T> list1,
        List<R> list2, BiPredicate<? super T, ? super R> biPredicate) {
        return new ListEquality<>(list1, list2)
            .testSizes()
            .compareAllByPredicate(biPredicate);
    }


    public static class ListEquality<T, R> {

        private final List<T> l1;
        private final List<R> l2;
        private boolean sizeEquals;
        private BiPredicate<List<T>, List<R>> allNulls;
        private BiPredicate<List<T>, List<R>> oneNull;
        private BiPredicate<List<T>, List<R>> sizes;
        private boolean allNullsResult = false;

        /**
         * Init this object for further list comparison.
         *
         * @param l1 input list
         * @param l2 input list
         */
        public ListEquality(List<T> l1, List<R> l2) {
            this.l1 = l1;
            this.l2 = l2;
            this.sizeEquals = true;
            this.allNulls = (list1, list2) -> list1 == null && list2 == null;
            this.oneNull = (list1, list2) -> list1 == null || list2 == null;
            this.sizes = (list1, list2) -> list1.size() == list2.size();
        }

        /**
         * Test sizes of input lists, use isOneNull to change logic for nulls.
         *
         * @return this
         */
        public ListEquality<T, R> testSizes() {
            //both nulls
            //if all are nulls
            this.allNullsResult = allNulls.test(l1, l2);
            boolean oneIsNull = false;
            if (this.allNullsResult) {
                //size are equals if both are nulls
                this.sizeEquals = true;
                return this;
            }
            //if one of lists is null
            oneIsNull = oneNull.test(l1, l2);
            if (oneIsNull) {
                //size is not equal if at least one is null
                //call isAllNulls in order to handle null checking in another way and prevent this part to be executed
                this.sizeEquals = false;
                return this;
            }
            //check sizes
            this.sizeEquals = sizes.test(l1, l2);
            return this;
        }

        /**
         * Override default size check (list1.size == list2.size).
         *
         * @param oneNull BiFunction to check if sizes are equal or not
         * @return this
         */
        public ListEquality<T, R> isOneNull(BiPredicate<List<T>, List<R>> oneNull) {
            this.oneNull = oneNull;
            return this;
        }

        /**
         * Override default size check (list1.size == list2.size).
         *
         * @param allNulls BiFunction to check if sizes are equal or not
         * @return this
         */
        public ListEquality<T, R> isAllNulls(BiPredicate<List<T>, List<R>> allNulls) {
            this.allNulls = allNulls;
            return this;
        }

        /**
         * Override default size check (list1.size == list2.size).
         *
         * @param sizesFunction BiFunction to check if sizes are equal or not
         * @return this
         */
        public ListEquality<T, R> sizes(BiPredicate<List<T>, List<R>> sizesFunction) {
            this.allNulls = sizesFunction;
            return this;
        }

        public ListEquality<T, R> treatNullAsEmpty() {
            this.allNulls = (left, right) -> (left == null || left.isEmpty()) && (right == null || right.isEmpty());
            return this;
        }

        /**
         * Compare lists by input predicate.
         *
         * @param biPredicate input predicate, with l1 and l2
         * @return boolean value if lists are equal when predicate passes with true
         */
        public boolean compareAllByPredicate(BiPredicate<? super T, ? super R> biPredicate) {
            if (sizeEquals && !this.allNullsResult) {
                return l1.stream()
                    .allMatch(lelm -> l2.stream()
                        .anyMatch(relm -> biPredicate.test(lelm, relm)));
            }
            return sizeEquals;
        }

        /**
         * Compare lists by input predicate.
         *
         * @param predicate input predicate, with l1
         * @return boolean value if lists are equal when predicate passes with true
         */
        public boolean compareAllByPredicate(Predicate<? super T> predicate) {
            if (sizeEquals && !this.allNullsResult) {
                return l1.stream()
                    .allMatch(predicate);
            }
            return false;
        }

        /**
         * Check if both lists are equal.
         * @return true if equals
         */
        public boolean areEquals() {
            if (sizeEquals && !this.allNullsResult) {
                return l1.containsAll(l2) && l2.containsAll(l1);
            }
            return sizeEquals;
        }

        /**
         * Compare lists by input predicate.
         *
         * @param filter input predicate, with elements - l1 and l2 which tells how to align fields
         * @param validation input BiConsumer, with l1 and l2 which will pass to user defined funcioon, i.e. assert
         * @return boolean value if lists are equal when predicate passes with true
         */
        public boolean filterAndTest(BiPredicate<? super T, ? super R> filter,
            BiConsumer<? super T, ? super R> validation) {
            if (sizeEquals && !this.allNullsResult) {
                l1.forEach(
                    lelm ->
                        validation.accept(lelm, l2
                            .stream()
                            .filter(relm -> filter.test(lelm, relm))
                            .findFirst().orElse(null)));
            }
            return sizeEquals;
        }

    }

}

package framework.utilities;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMap {

    private FlatMap() {
        //default constructor
    }

    /**
     * Flatten a map.
     * @param map Map
     * @return flat map; complex map becomes flat
     */
    public static Map<String, Object> asFlattendMap(Map<String, Object> map) {
        return map.entrySet().stream()
            .flatMap(FlatMap::flat)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Stream<Map.Entry<String, ?>> flat(Map.Entry<String, ?> entry) {
        if (entry.getValue() instanceof Map) {
            return ((Map<String, ?>) entry.getValue()).entrySet().stream().flatMap(FlatMap::flat);
        }
        return Stream.of(entry);
    }

}

package framework.utilities.softasst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import framework.utilities.BeanUtils;

public class JsonCompare {

    private final List<String> diffs;

    public JsonCompare() {
        this.diffs = new ArrayList<>();
    }

    public List<String> getDiffs() {
        return diffs;
    }

    public String getDiffsMessage() {
        return this.getDiffs().stream()
            .collect(Collectors.joining("\n"));
    }

    /**
     * Check if input objects are equal.
     */
    public boolean isEqual(Object actual, Object expected) {
        this.diffs.clear();
        if (actual instanceof Map && expected instanceof Map) {
            compare((Map) actual, (Map) expected);
        } else if (actual instanceof List && expected instanceof List) {
            compare("<root>", (List) actual, (List) expected);
        } else {
            this.diffs.add("Left object type is not the same as right object type. Convert JSON Object to Map.");
        }
        return diffs.isEmpty();
    }

    /**
     * Compare object with other object ignore or not ignore null fields Comparing complex object.
     */
    public <T> boolean equalsIgnoringNullFields(T actual, T expected) {
        Map<String, String> nonNullFields = BeanUtils.recursiveDescribe(expected);
        Map<String, String> thisFields = BeanUtils.recursiveDescribe(actual);
        for (Map.Entry<String, String> entry : nonNullFields.entrySet()) {
            if (!thisFields.containsKey(entry.getKey())) {
                this.diffs.add("--- Field " + entry.getKey() + " not found \n");
            }
            Object otherValue = BeanUtils.getProperty(expected, entry.getKey());
            Object actualValue = BeanUtils.getProperty(actual, entry.getKey());
            if (!otherValue.equals(actualValue)) {
                this.diffs.add(
                    "--- Field " + entry.getKey() + "\n" + "Actual: " + actualValue + "\n" + "Expected:" + otherValue
                        + "\n");
            }
        }
        return diffs.isEmpty();
    }

    private void compare(Map<String, Object> actual, Map<String, Object> expected) {

        for (Map.Entry<String, Object> expectedEntry : expected.entrySet()) {
            Object actualValue = actual.getOrDefault(expectedEntry.getKey(), null);

            if (actualValue != null) {
                if (expectedEntry.getValue() instanceof Map) {
                    compare((Map) actualValue, (Map) expectedEntry.getValue());
                } else if (expected instanceof List) {
                    compare(expectedEntry.getKey(), (List) actualValue, (List) expectedEntry.getValue());
                } else if (!expectedEntry.getValue().equals(actualValue)) {
                    diffs.add(String.format("Validate field <%s>: expected: <%s> but was: <%s>",
                        expectedEntry.getKey(), expectedEntry.getValue(), actualValue));
                }
            }
        }

    }

    private void compare(String parentKey, List<Object> actual, List<Object> expected) {
        for (Object expectedElement : expected) {
            Object actualElement = null;
            int actualElementIndex =
                actual.size() >= expected.indexOf(expectedElement) ? expected.indexOf(expectedElement) : -1;
            if (actualElementIndex > -1) {
                actualElement = actual.get(actualElementIndex);
            }

            if (actualElement instanceof Map) {
                compare((Map) actualElement, (Map) expectedElement);
            } else if (actualElement instanceof List) {
                compare(parentKey, (List) actualElement, (List) expectedElement);
            } else if (actualElement != null && !expectedElement.equals(actualElement)) {
                diffs.add(String.format("Validate field: <%s>: expected: <%s> but was: <%s>",
                    parentKey, expectedElement, actualElement));
            }
        }
    }

}

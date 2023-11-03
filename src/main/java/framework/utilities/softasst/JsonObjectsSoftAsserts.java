package framework.utilities.softasst;

import java.util.List;
import org.assertj.core.api.AbstractAssert;

public class JsonObjectsSoftAsserts extends AbstractAssert<JsonObjectsSoftAsserts, Object> {

    public JsonObjectsSoftAsserts(Object actual) {
        super(actual, JsonObjectsSoftAsserts.class);
    }

    /**
     * Compare objects - left mode.
     * @param expected Object
     * @return JsonObjectsSoftAsserts
     */
    public JsonObjectsSoftAsserts compareWithLeftMode(Object expected) {

        JsonCompare comparingAction = new JsonCompare();
        comparingAction.isEqual(actual, expected);
        List<String> result = comparingAction.getDiffs();
        if (!result.isEmpty()) {
            StringBuilder customizeMessage = new StringBuilder();
            result.forEach(message -> customizeMessage.append(message).append("\n"));
            failWithMessage("Differences failed:\n%s", customizeMessage.toString().trim());
        }
        return this;
    }
}

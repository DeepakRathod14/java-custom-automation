package framework.utilities.softasst;

import org.assertj.core.api.SoftAssertions;

public class AutomationSoftAsserts extends SoftAssertions {

    /**
     * If lambda asserts are needed, call wrap() method to access Lambda asserts.
     *
     * @param actual WrappedObject; WrappedObject.wrap(myObject)
     * @return LambdaAsserts {@literal <}T{@literal >}
     */
    public <T> LambdaAsserts<T> assertThat(WrappedObject<T> actual) {
        return proxy(LambdaAsserts.class, WrappedObject.class, actual);
    }

    /**
     * Assert jsons.
     * Example: softAssert.assertJson(retrievedVersionA).compareWithLeftMode(retrievedVersionB)
     * @param object Object
     * @return JsonObjectsSoftAsserts
     */
    public JsonObjectsSoftAsserts assertJson(Object object) {
        return proxy(JsonObjectsSoftAsserts.class, Object.class, object);
    }

}

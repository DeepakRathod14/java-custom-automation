package framework.utilities.softasst;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.assertj.core.api.AbstractAssert;

public class LambdaAsserts<T> extends AbstractAssert<LambdaAsserts<T>, T> {


    public LambdaAsserts(WrappedObject<T> actual) {
        super(actual.getObject(), LambdaAsserts.class);
    }

    /**
     * Assertion with BiPredicate with input params (actual and expected).
     */
    public <U> LambdaAsserts<T> matchesBiFunction(U expected, BiPredicate<T, U> function) {
        boolean result = function.test(this.actual, expected);

        if (!result) {
            failWithMessage("Expecting Object to match against BiFunction\n actual: %s \n But was: %s",
                actual,
                expected);
        }
        return this;
    }

    /**
     * Assertion with Predicate, input for predicate is Actual.
     */
    public LambdaAsserts<T> matchesFunction(Predicate<T> expected) {
        boolean result = expected.test(this.actual);

        if (!result) {
            failWithMessage("Expecting Object to match against Function\n actual: %s \n But was: %s",
                actual,
                expected);
        }
        return this;
    }
}

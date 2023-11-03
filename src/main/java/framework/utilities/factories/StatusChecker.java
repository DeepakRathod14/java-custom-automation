package framework.utilities.factories;

import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Extend WaitFactory with return Object data during execution until.
 * Example
 * <pre>
 *      StatusChecker&#60;StatusEndpointDto&#62; awaitPatches = new StatusChecker<>();
 *      awaitPatches.polling(() -> patchingApi().thirdparty().status().getEndpointStatus(partnerId, endpointId));
 *      WaitFactory.createAwait()
 *          .atMost(new Duration(timeout, TimeUnit.SECONDS))
 *          .conditionEvaluationListener(condition ->
 *              LOGGER.debug("Waiting for Missed Patches NOT to be EMPTY, but actual is <{}>",condition.getValue()))
 *      .until(awaitPatches.checkStatus(missedPatches -> isPatchMissed(app, missedPatches)), Matchers.is(true));
 *      endpointStatus = awaitPatches.getResult();
 * </pre>
 */
public class StatusChecker<T> {

    private Supplier<T> supplier;
    private T result;

    /**
     * Poll operation that will be passes as input parameter into checkStatus().
     *
     * @param supplier Supplier of input objects
     */
    public void polling(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Get result of checking operation.
     *
     * @return T
     */
    public T getResult() {
        return result;
    }

    /**
     * Check status of predicate.
     *
     * @param function predicate to be tested
     * @return Callable of boolean
     */
    public Callable<Boolean> checkStatus(Predicate<T> function) {
        return () -> {
            this.result = supplier.get();
            return function.test(this.result);
        };
    }
}

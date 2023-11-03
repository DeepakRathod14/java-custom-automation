package framework.utilities.factories;

import static org.awaitility.Awaitility.with;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.core.ConditionFactory;

/**
 * Creates re-try mechanism with Duration.ONE_SECOND interval between attempts.
 */
public class WaitFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    private WaitFactory() {
        ///empty default constructor
    }

    /**
     * Create default ConditionFactory.
     *
     * @return ConditionFactory
     */
    public static ConditionFactory createAwait() {
        return with()
            .conditionEvaluationListener(condition ->
                LOGGER.debug(String.format("%s (elapsed time %dms, remaining time %dms)",
                    condition.getDescription(),
                    condition.getElapsedTimeInMS(),
                    condition.getRemainingTimeInMS()))
            ).await()
            .atMost(Duration.ofSeconds(30))
            .pollInterval(Duration.ofSeconds(1))
            .pollDelay(Duration.ofSeconds(1))
            .pollInSameThread();
    }

    /**
     * Execute await operation with given ConditionFactory.
     *
     * @param condition ConditionFactory to be executed
     */
    public static void executeWait(ConditionFactory condition) {
        AtomicBoolean finished = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                finished.set(true);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        condition.untilTrue(finished);
    }

    /**
     * Delay execution of current thread for defined period of time in seconds.
     *
     * @param seconds duration in seconds
     */
    public static void delay(int seconds) {
        LOGGER.debug("Wait [{}] seconds", seconds);
        createAwait()
            .conditionEvaluationListener(null)
            .pollInSameThread()
            .pollDelay(Duration.ofSeconds(seconds))
            .atMost(Duration.ofSeconds(seconds+2L))
            .until(() -> true);
    }

    /**
     * Delay execution of current thread for defined period of time.
     *
     * @param duration Duration
     */
 /*   public static void delay(Duration duration) {
        createAwait()
            .pollInSameThread()
            .pollDelay(duration)
            .atMost(duration.get(duration.))
            .until(() -> true);
    }*/
}

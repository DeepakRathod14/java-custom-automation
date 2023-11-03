package framework.utilities;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExecutorHelper {

    private ExecutorHelper() {
    }

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Stop executor service.
     *
     * @param executor Executor service to be stoppped
     * @return boolean representing result of this operation
     */
    public static boolean stopExecutor(ExecutorService executor) {
        boolean isFinished = false;
        try {
            executor.shutdown();
            final boolean done = executor.awaitTermination(10, TimeUnit.SECONDS);
            LOGGER.debug("is verything was executed? {}", done);
            isFinished = done;
        } catch (InterruptedException e) {
            LOGGER.error("termination interrupted");
            Thread.currentThread().interrupt();
        } finally {
            if (!executor.isTerminated()) {
                LOGGER.debug("killing non-finished tasks");
            }
            final List<Runnable> rejected = executor.shutdownNow();
            LOGGER.debug("rejected: {}", rejected.size());
            isFinished = rejected.isEmpty();
        }
        return isFinished;
    }
}

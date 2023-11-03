package framework.utilities.factories;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A simple helper to execute callable tasks in parallel.
 */
public class ThreadExecutorHelper {

    private final int numberOfThreads;
    ExecutorService executor;

    private static final Logger LOGGER = LogManager.getLogger();

    private ThreadExecutorHelper() {
        this(1);
    }

    /**
     * Create executor with defined number of threads.
     *
     * @param numberOfThreads number of threads
     */
    public ThreadExecutorHelper(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setDaemon(true)
            .build();

        executor = Executors.newFixedThreadPool(this.numberOfThreads, threadFactory);
    }

    public static ThreadExecutorHelper threadExecutor(int threads) {
        return new ThreadExecutorHelper(threads);
    }

    /**
     * Execute task.
     *
     * @param callableTask input task to be executed
     * @param <T> Any object to process
     */
    public <T> void executeTask(Callable<T> callableTask) {
        this.executeTask(this.numberOfThreads, callableTask);
    }

    /**
     * Execute tasks in parallel.
     *
     * @param numberOfTasks - number of tasks to be executed in parallel
     * @param callableTask - a task to call
     * @param <T> Any object to process
     */
    public <T> void executeTask(final int numberOfTasks, Callable<T> callableTask) {
        List<Callable<T>> callables = new ArrayList<>();
        for (int task = 0; task < numberOfTasks; task++) {
            callables.add(callableTask);
        }
        try {
            executor.invokeAll(callables)
                .forEach(
                    this::getCompleted
                );
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            LOGGER.error(e);
        }

        executor.shutdown();
    }

    /**
     * Return completed operation from Future.get.
     *
     * @param future Future
     * @param <T> any type to be processed and returned
     * @return T
     */
    private <T> T getCompleted(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            LOGGER.error(e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException | RuntimeException e) {
            LOGGER.error(e);
        }
        return null;
    }
}

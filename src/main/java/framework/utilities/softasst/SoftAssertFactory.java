package framework.utilities.softasst;


/**
 * Handles soft assert objects for different threads.
 */
public class SoftAssertFactory {

    private static final ThreadLocal<AutomationSoftAsserts> tlSoftAssert = new ThreadLocal<>();

    private SoftAssertFactory() {
    }

    /**
     * Retrieve assertions for current thread.
     * @return AutomationSoftAsserts
     */
    public static AutomationSoftAsserts getSoftAssert() {
        if (tlSoftAssert.get() == null) {
            createSoftAssert();
        }
        return tlSoftAssert.get();
    }

    /**
     * Execute assert all in current thread.
     */
    public static void assertAll() {
        AutomationSoftAsserts ajSoftAssert = tlSoftAssert.get();

        if (ajSoftAssert != null) {
            ajSoftAssert.assertAll();
        }
    }

    public static void reset() {
        tlSoftAssert.remove();
    }

    private static void createSoftAssert() {
        tlSoftAssert.set(new AutomationSoftAsserts());
    }
}

package framework.utilities.primitive.generators;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class StringGenerator implements IPrimitiveGenerator<String, Long> {

    private static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "~!@#$%^&*() ";
    private static final String ALPHA_NUMERIC = AB + NUMERIC;
    private static final String ALPHA_NUMERIC_WITH_SPACE = " " + ALPHA_NUMERIC;
    private static final String ALPHA_NUMERIC_WITH_SPECIAL_CHARS = ALPHA_NUMERIC + SPECIAL_CHARS;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generate random alpha String.
     *
     * @param len size of desired random string
     * @return generated String
     */
    public static String randomAlpha(final int len) {
        return randomize(AB, len);
    }

    /**
     * Generate random alpha numeric String.
     *
     * @param len size of desired random string
     * @return generated String
     */
    public static String randomAlphaNumeric(int len) {
        return randomize(ALPHA_NUMERIC, len);
    }

    /**
     * Generate random alpha numeric String.
     *
     * @param len size of desired random string
     * @return generated String
     */
    public static String randomAlphaNumericWithSpaces(int len) {
        return randomize(ALPHA_NUMERIC_WITH_SPACE, len).trim();
    }

    /**
     * Generate random alpha numeric String with special chars.
     *
     * @param len size of desired random string
     * @return generated String
     */
    public static String randomAlphaNumericWithSpecialChars(int len) {
        return randomize(ALPHA_NUMERIC_WITH_SPECIAL_CHARS, len).trim();
    }

    /**
     * Generate random numeric string.
     *
     * @param len size of desired random string
     * @return generated String
     */
    public static String randomNumeric(int len) {
        return randomize(NUMERIC, len);
    }

    private static String randomize(String charCollection, int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(charCollection.charAt(SECURE_RANDOM.nextInt(charCollection.length())));
        }
        return sb.toString();
    }

    /**
     * generate random uuid.
     *
     * @return String
     */
    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generate random uuid.
     *
     * @param length len
     * @param spacing spacing
     * @param spacerChar space char
     * @return String
     */
    public static String randomUuid(int length, int spacing, char spacerChar) {
        StringBuilder sb = new StringBuilder();
        int spacer = 0;
        while (length > 0) {
            if (spacer == spacing) {
                sb.append(spacerChar);
                spacer = 0;
            }
            length--;
            spacer++;
            sb.append(randomChar());
        }
        return sb.toString();
    }

    private static char randomChar() {
        return AB.charAt(SECURE_RANDOM.nextInt(AB.length()));
    }

    @Override
    public String generate() {
        return randomString(8L, 8L);
    }

    @Override
    public String generate(Long min, Long max) {
        return randomString(min, max);
    }

    private String randomString(long minLen, long maxLen) {
        long len = minLen == maxLen ? maxLen : ThreadLocalRandom.current().nextLong(minLen, maxLen);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(SECURE_RANDOM.nextInt(AB.length())));
        }
        return sb.toString();
    }

}

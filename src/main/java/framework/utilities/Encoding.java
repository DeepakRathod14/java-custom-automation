package framework.utilities;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Encoding {

    private static final Logger LOGGER = LogManager.getLogger();

    private Encoding() {
        //default constructor
    }

    /**
     * Encode input string to Base64.
     *
     * @param str input string
     * @return Base64 encoded string
     */
    public static String encodeStringToBase64(String str) {
        LOGGER.debug("Encode data to Base64: \n{}", str);
        byte[] bytesEncoded = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        String result = new String(bytesEncoded, StandardCharsets.UTF_8);
        LOGGER.debug("Encoded data to Base64: \n{}", result);
        return result;
    }

    /**
     * Decode input string from Base64.
     *
     * @param str input string in Base64
     * @return decoded string from Base64
     */
    public static String decodeStringFromBase64(String str) {
        LOGGER.debug("Decode data from Base64: \n{}", str);
        byte[] bytesDecoded = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        String result = new String(bytesDecoded, StandardCharsets.UTF_8);
        LOGGER.debug("Decoded data from Base64: \n{}", result);
        return result;
    }
}

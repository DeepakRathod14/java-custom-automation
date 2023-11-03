package framework.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StreamManager {

    protected static final Logger LOGGER = LogManager.getLogger(StreamManager.class);

    private StreamManager() {
    }

    /**
     * Close streams.
     *
     * @param varargs streams
     */
    public static void closeStreams(Object... varargs) {

        try {
            for (Object item : varargs) {
                if (item instanceof BufferedReader) {
                    ((BufferedReader) item).close();
                } else if (item instanceof BufferedWriter) {
                    ((BufferedWriter) item).close();
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Parse data from output in console (console).
     *
     * @param reader BufferedReader
     * @return String
     */
    public static String readOutput(BufferedReader reader) {
        StringBuilder textBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return textBuilder.toString().trim();
    }

    /**
     * Write command to console.
     *
     * @param writer  BufferedWriter
     * @param command command to write
     */
    public static void writeToConsole(BufferedWriter writer, String command) {
        try {
            writer.write(command);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Read input stream into StringBuilder.
     *
     * @param stream input stream
     * @return StringBuilder
     */
    public static StringBuilder readInputStream(InputStream stream) {
        StringBuilder outputResult = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String errorOutput = StreamManager.readOutput(reader);
            if (StringUtils.isNotEmpty(errorOutput)) {
                outputResult.append(errorOutput);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return outputResult;
    }

}

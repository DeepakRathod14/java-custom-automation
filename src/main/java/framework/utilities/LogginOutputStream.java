package framework.utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogginOutputStream extends OutputStream {

	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Default number of bytes in the buffer.
	 */
	private static final int DEFAULT_BUFFER_LENGTH = 2048;

	/**
	 * Indicates stream state.
	 */
	private boolean hasBeenClosed = false;

	/**
	 * Internal buffer where data is stored.
	 */
	private byte[] buf;

	/**
	 * The number of valid bytes in the buffer.
	 */
	private int count;

	/**
	 * Remembers the size of the buffer.
	 */
	private int curBufLength;

	/**
	 * The LOGGER to write to.
	 */
	private Logger log;

	/**
	 * The log level.
	 */
	private Level level;

	/**
	 * Creates the Logging instance to flush to the given LOGGER.
	 *
	 * @param log   the Logger to write to
	 * @param level the log level
	 * @throws IllegalArgumentException in case if one of arguments is null.
	 */
	public LogginOutputStream(final Logger log, final Level level) {
		if (log == null || level == null) {
			throw new IllegalArgumentException("Logger or log level must be not null");
		}
		this.log = log;
		this.level = level;
		curBufLength = DEFAULT_BUFFER_LENGTH;
		buf = new byte[curBufLength];
		count = 0;
	}

	/**
	 * Redirect sout to logger.
	 */
	public static void redirectPrintsToLogger() {
		try {
			System.setErr(new PrintStream(new LogginOutputStream(LogManager.getLogger("outLog"), Level.INFO), true,
					StandardCharsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Writes the specified byte to this output stream.
	 *
	 * @param b the byte to write
	 * @throws IOException if an I/O error occurs.
	 */
	@Override
	public void write(final int b) throws IOException {
		if (hasBeenClosed) {
			throw new IOException("The stream has been closed.");
		}
		// don't log nulls
		if (b == 0) {
			return;
		}
		// would this be writing past the buffer?
		if (count == curBufLength) {
			// grow the buffer
			final int newBufLength = curBufLength + DEFAULT_BUFFER_LENGTH;
			final byte[] newBuf = new byte[newBufLength];
			System.arraycopy(buf, 0, newBuf, 0, curBufLength);
			buf = newBuf;
			curBufLength = newBufLength;
		}

		buf[count] = (byte) b;
		count++;
	}

	/**
	 * Flushes this output stream and forces any buffered output bytes to be written
	 * out.
	 */
	@Override
	public void flush() {
		if (count == 0) {
			return;
		}
		final byte[] bytes = new byte[count];
		System.arraycopy(buf, 0, bytes, 0, count);
		String str = new String(bytes, StandardCharsets.UTF_8);
		log.log(level, str);
		count = 0;
	}

	/**
	 * Closes this output stream and releases any system resources associated with
	 * this stream.
	 */
	@Override
	public void close() {
		flush();
		hasBeenClosed = true;
	}

}

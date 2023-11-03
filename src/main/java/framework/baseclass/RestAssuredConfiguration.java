package framework.baseclass;

import java.util.concurrent.TimeUnit;
import static io.restassured.config.HttpClientConfig.httpClientConfig;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;

public class RestAssuredConfiguration {

	protected static final Logger LOGGER = LogManager.getLogger();
	private final RestAssuredConfig config;

	private RestAssuredConfiguration() {
		
		this.config = RestAssuredConfig.newConfig().httpClient(httpClientConfig().httpClientFactory(() -> {
			PoolingClientConnectionManager pool2 = new PoolingClientConnectionManager();
			pool2.setMaxTotal(10000);
			pool2.setDefaultMaxPerRoute(300);
			iddleMonitor(pool2);

			DefaultHttpClient client = new DefaultHttpClient(pool2);

			client.setKeepAliveStrategy(this::configureKeepAlive);

			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 60 * 1000);
			HttpConnectionParams.setSoTimeout(params, 60 * 1000);
			HttpConnectionParams.setSoReuseaddr(params, true);
			HttpConnectionParams.setTcpNoDelay(params, true);
			return client;
		}).setParam("http.connection.timeout", 300000).setParam("http.socket.timeout", 300000)
				.setParam("http.connection-manager.timeout", 300000).reuseHttpClientInstance())
				.encoderConfig(initDefaultEncoderConfig()).sslConfig(new SSLConfig().allowAllHostnames());
	}

	public static RestAssuredConfiguration instance() {
		return SingletonHolder.INSTANCE;
	}

	private long configureKeepAlive(HttpResponse response, HttpContext context) {
		// Honor 'keep-alive' header
		HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
		while (it.hasNext()) {
			HeaderElement he = it.nextElement();
			String param = he.getName();
			String value = he.getValue();
			if (value != null && param.equalsIgnoreCase("timeout")) {
				try {
					return Long.parseLong(value) * 1000;
				} catch (NumberFormatException ignore) {
					LOGGER.trace("There was an error parsing keep alive headers");
				}
			}
		}

		// should be configured differently based on host, etc.
		return 60L * 1000;
	}

	/**
	 * Will use once rest assured moves to newer implementations.
	 */
	private void iddleMonitor(PoolingClientConnectionManager pool) {
		IdleConnectionMonitorThread staleMonitor = new IdleConnectionMonitorThread(pool);
		staleMonitor.start();
		try {
			staleMonitor.join(1000);
		} catch (InterruptedException e) {
			LOGGER.error(e);
			Thread.currentThread().interrupt();
		}
	}

	public RestAssuredConfig defaultConfig() {
		return this.config;
	}

	private EncoderConfig initDefaultEncoderConfig() {
		return RestAssured.config().getEncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(true);
	}

	private static class SingletonHolder {

		private static final RestAssuredConfiguration INSTANCE = new RestAssuredConfiguration();
	}

	public static class IdleConnectionMonitorThread extends Thread {

		private final ClientConnectionManager connMgr;
		private volatile boolean shutdown;

		public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
			super();
			this.connMgr = connMgr;
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						wait(1000);
						connMgr.closeExpiredConnections();
						connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
					}
				}
			} catch (InterruptedException ex) {
				shutdown();
				Thread.currentThread().interrupt();
			}
		}

		/**
		 * Shutdown idle connections monitor thread.
		 */
		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}

	}

}

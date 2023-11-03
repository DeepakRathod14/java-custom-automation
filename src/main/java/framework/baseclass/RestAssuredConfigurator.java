package framework.baseclass;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;

import framework.utilities.LogginOutputStream;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredConfigurator {
	protected static final Logger LOGGER = LogManager.getLogger();
	private final RequestSpecBuilder specBuilder;

	private RestAssuredConfigurator(RequestSpecBuilder builder) {
		specBuilder = builder;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public RequestSpecification getRequestSpecification() {
		return specBuilder.build();
	}

	public RequestSpecBuilder getSpecBuilder() {
		return specBuilder;
	}

	public static final class Builder {
		private boolean usingDefaultFilters;
		private RequestSpecBuilder specBuilder;
		private String hostname;
		private Integer apiPort;
		private String basePath;
		private RestAssuredConfig config;
		private List<Filter> filters;
		private String contentTypeStr;
		private ContentType contentType;
		private Boolean followRedirects;
		private boolean usingHeaders;
		private Map<String, String> headers;
		private URI uri;

		private Builder() {

			specBuilder = new RequestSpecBuilder();
			this.hostname = "http://localhost";
			this.apiPort = 80;
			this.config = null;
			this.contentType = ContentType.ANY;
			this.headers = new HashMap<>();
			this.followRedirects = true;
			this.usingDefaultFilters = true;
			this.filters = new ArrayList<>();
			this.usingHeaders = false;
			this.uri = null;
		}

		public Builder withSpecBuilder(RequestSpecBuilder val) {
			specBuilder = val;
			return this;
		}

		public Builder usingCustomConfig(RestAssuredConfig config) {
			this.config = config;
			return this;
		}

		/**
		 * Enable Request Filter. This filter will log all requests into console in
		 * Debug mode.
		 * 
		 * @return this
		 */
		public Builder usingRequestFilter() {
			try {
				return this.usingFilter(new RequestLoggingFilter(
						new PrintStream(new LogginOutputStream(LogManager.getLogger(), Level.DEBUG), true,
								StandardCharsets.UTF_8.name())));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e);
			}
			return this;
		}

		/**
		 * Enable Response Filter. This filter will log all responses into console in
		 * Debug mode.
		 * 
		 * @return this
		 */
		public Builder usingResponseFilter() {
			try {
				return this.usingFilter(new ResponseLoggingFilter(
						new PrintStream(new LogginOutputStream(LogManager.getLogger(), Level.DEBUG), true,
								StandardCharsets.UTF_8.name())));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e);
			}
			return this;
		}

		/**
		 * Enable Error Filter. This filter will log all errors into console in Error
		 * mode.
		 * 
		 * @return this
		 */
		public Builder usingErrorLogFilter() {
			try {
				return this.usingFilter(new ErrorLoggingFilter(
						new PrintStream(new LogginOutputStream(LogManager.getLogger(), Level.ERROR), true,
								StandardCharsets.UTF_8.name())));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e);
			}
			return this;
		}

//		/**
//		 * Enable RestAssured Filter.
//		 * 
//		 * @return this
//		 */
//		public Builder usingAllureFilter() {
//			return this.usingFilter(new AllureRestAssured());
//		}

		/**
		 * Define list of filters instead of using default.
		 * 
		 * @param filters Filter list
		 * @return this
		 */
		public Builder usingCustomFilters(List<Filter> filters) {
			this.usingDefaultFilters = false;
			this.filters = filters;
			return this;
		}

		/**
		 * Define rest assured filter to use.
		 * 
		 * @param filter Filter
		 * @return Builder
		 */
		public Builder usingFilter(Filter filter) {
			this.usingDefaultFilters = false;
			this.filters.add(filter);
			return this;
		}

		public Builder followRedirects(Boolean follow) {
			this.followRedirects = follow;
			return this;
		}

		public Builder usingContentType(String contentType) {
			this.contentTypeStr = contentType;
			return this;
		}

		public Builder usingContentType(ContentType contentType) {
			this.contentType = contentType;
			return this;
		}

		/**
		 * Define custom headers.
		 * 
		 * @param headers Map
		 * @return this
		 */
		public Builder withHeaders(Map<String, String> headers) {
			this.usingHeaders = true;
			if (headers != null) {
				this.headers = headers;
			}
			return this;
		}

		/**
		 * Define connection info.
		 * 
		 * @param apiUrl   base path, hostname, ip, etc
		 * @param apiPort  api port
		 * @param basePath the base path, e.g someapi/v1/
		 * @return this
		 */
		public Builder withConnectionInfo(String apiUrl, Integer apiPort, String... basePath) {
			this.hostname = apiUrl;
			this.apiPort = apiPort;
			this.basePath = String.join("/", basePath);
			return this;
		}

		/**
		 * Build RestAssuredConfigurator object that holds RestAssured specification.
		 * 
		 * @return RestAssuredConfigurator
		 */
		public RestAssuredConfigurator build() {
			setupConfiguration();
			setupHeaders();
			setupContentType();
			setupConnectionInfo();
			createDefaultResponseSpecification();
			return new RestAssuredConfigurator(specBuilder);
		}

		private static void createDefaultResponseSpecification() {
			// Default response specification, a simple trick so that connections, which
			// responses with Content-Length
			// that are 0 (zero) would be closed and released from pool
			RestAssured.responseSpecification = new ResponseSpecBuilder()
					.expectBody(Matchers.anyOf(Matchers.notNullValue(), Matchers.nullValue())).build();
		}

		private void setupHeaders() {
			if (usingHeaders) {
				headers.forEach((hk, hv) -> this.specBuilder.addHeader(hk, hv));
			}
		}

		private void setupConfiguration() {
			if (this.config == null) {
				if (Boolean.FALSE.equals(this.followRedirects)) {
					RestAssuredConfiguration.instance().defaultConfig()
							.redirect(RedirectConfig.redirectConfig().followRedirects(false));
				} else {
					specBuilder.setConfig(RestAssuredConfiguration.instance().defaultConfig());
				}
			} else {
				specBuilder.setConfig(this.config);
			}
		}

		private void setupContentType() {
			if (this.contentTypeStr != null) {
				specBuilder.setContentType(contentTypeStr);
			} else {
				specBuilder.setContentType(contentType);
			}
		}

		private void setupConnectionInfo() {
			if (this.uri == null) {
				specBuilder.setBaseUri(this.hostname);
				if (apiPort != null) {
					specBuilder.setPort(this.apiPort);
				}
				if (!StringUtils.isEmpty(this.basePath)) {
					specBuilder.setBasePath(basePath);
				}
			} else {
				this.specBuilder.setBaseUri(this.uri);
				this.specBuilder.setPort(uri.getPort());
			}
		}

		/**
		 * Define URI from string as an alternative way to build connection info
		 * (withConnectionInfo method).
		 * 
		 * @param source String
		 * @return this
		 */
		public Builder withUri(String source) {
			this.uri = getUrl(source);
			return this;
		}

		private URI getUrl(String url) {
			try {
				return new URI(url);
			} catch (URISyntaxException e) {
				LOGGER.error(e);
			}
			return null;
		}

	}

}

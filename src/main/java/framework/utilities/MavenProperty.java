package framework.utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class MavenProperty {

	protected static final Logger LOGGER = LogManager.getLogger();

	public static final String FALSE = "false";
	private static final String REPORT_PORTAL_PROPERTIES = "reportportal.properties";
	private static final String MAVEN_PROPERTIES = "maven.properties";
	private static final String ENVIRONMENT_PROPERTY = "AutomationEnvironment";
	private Properties properties;

	private MavenProperty() {
		loadProperties();
	}

	public static Properties getMavenProperties() {
		return SingletonHolder.INSTANCE.properties;
	}

	public static String getCurrentEnvironment() {
		return getMavenProperties().getProperty(ENVIRONMENT_PROPERTY);
	}

	/**
	 * Get a value from `TestRailReporter.createTestRunsForDataProviderVariations`
	 * maven property in maven.properties Default is false. Means that Values
	 * returned in TestNG data provider will affect the name of test rail For
	 * example if Data Provider returns, three values such as win7, win10, win2012
	 * then listener will create 3 Test Rail reports with corresponding windows
	 * names in name
	 *
	 * @return Boolean
	 */
	public static boolean isCreateTestRunsForDataProviderVariations() {
		String prop = getMavenProperties().getProperty("TMSReporter.createTestRunsForDataProviderVariations", FALSE);
		return Boolean.parseBoolean(prop);
	}

	public static String getTM4JiraSpace() {
		return MavenProperty.getMavenProperties().getProperty("TM4JiraSpace");
	}

	/**
	 * Check if report portal is enabled. System property used - rp.enable.
	 *
	 * @return true if report portal is enabled
	 */
	public static boolean isReportPortalEnabled() {
		String prop = getMavenProperties().getProperty("rp.enable", FALSE);
		return Boolean.parseBoolean(prop);
	}

	public static boolean isJenkins() {
		return Boolean.parseBoolean(MavenProperty.getMavenProperties().getProperty("jenkins", FALSE));
	}

	private void loadProperties() {
		properties = new Properties();
		properties.putAll(loadProp(REPORT_PORTAL_PROPERTIES));
		properties.putAll(loadProp(MAVEN_PROPERTIES));
		Properties systemProperties = System.getProperties();
		properties.putAll(systemProperties);
	}

	private ImmutableMap<String, String> loadProp(String propertyFileName) {
		File inputFile = new File("src/main/resources/" + propertyFileName);
		try (FileReader reader = new FileReader(inputFile.getAbsolutePath())) {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Maps.fromProperties(properties);
	}

	private static class SingletonHolder {
		static final MavenProperty INSTANCE = new MavenProperty();

		private SingletonHolder() {
			// empty
		}
	}
}

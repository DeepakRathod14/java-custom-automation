package framework.baseclass;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;

import framework.bean.apiconfig.ApiConfig;
import framework.utilities.BeanUtils;
import framework.utilities.MavenProperty;
import framework.utilities.config.ConfigLoader;
import framework.utilities.objectmapper.DtoConvert;

public class EnvironmentProvider {
    protected static final Logger LOGGER = LogManager.getLogger();
    private static final String ENVIRONMENT_TEMPLATE = "/Environments/%s/%s";
    private static final String ENVIRONMENTS_FILE = "environment.json";
    private static final String ADAPTIVIST_MAP = "/Report/Adaptivist.json";

    private Map<String, Object> environments;
    private Map<String, Map<String, Object>> jiraReportersMap;

    private EnvironmentProvider() {
        String environment = MavenProperty.getCurrentEnvironment();
        if (StringUtils.isEmpty(environment)) {
            environment = "dev";
        }
        LOGGER.info("Automation tests run on Environment: [{}]", environment.toUpperCase());
        loadEnvironmentFiles(environment);
    }

    public static EnvironmentProvider provideEnvironment() {
        return SingletonHolder.INSTANCE;
    }

    private Map<String, Object> getEnvironments() {
        return environments;
    }

    private void loadEnvironmentFiles(String environment) {
        loadEnvironmentDefinitions(environment);
        loadAdaptivistDefinitions();
    }

    private void loadEnvironmentDefinitions(String environment) {
        final String environmentFile = String.format(ENVIRONMENT_TEMPLATE, environment, ENVIRONMENTS_FILE);
        LOGGER.debug("Read environments file from resources: [{}]", environmentFile);
        this.environments = DtoConvert.jsonFileToDto(environmentFile, new TypeReference<Map<String, Object>>() {
        });
        Objects.requireNonNull(this.environments);
        ConfigLoader.overrideObjectBySystemPropertyRecursively(this.environments);
    }

    private void loadAdaptivistDefinitions() {
        LOGGER.debug("Read reporter file from resources: [{}]", ADAPTIVIST_MAP);
        this.jiraReportersMap = DtoConvert.jsonFileToDto(
            ADAPTIVIST_MAP, new TypeReference<Map<String, Map<String, Object>>>() {
            });
        if (null != this.jiraReportersMap) {
            ConfigLoader.overrideObjectBySystemPropertyRecursively(this.jiraReportersMap);
        }
    }

    public Map<String, Map<String, Object>> getJiraReportersMap() {
        return jiraReportersMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T getSettings(String settingName) {
        return (T) this.getEnvironments().get(settingName);
    }

    /**
     * Retrieve api url for input setting (environment.json file).
     *
     * @param settingName api url
     * @return
     */
    public String getApiUrl(String settingName) {
        Object settings = this.getEnvironments().get(settingName);
        if (settings instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) settings;
            ApiConfig config = DtoConvert.mapToDto(ApiConfig.class, map, false);
            if (config == null) {
                throw new IllegalStateException("Config not found");
            }
            return String.format(
                "%s:%s",
                config.getApiUrl(),
                config.getApiPort());
        } else {
            throw new IllegalArgumentException("Unknown format for given key");
        }
    }

    public Object getSetting(String settingName) {
        return BeanUtils.getProperty(this.getEnvironments(), settingName);
    }

    private static class SingletonHolder {

        static final EnvironmentProvider INSTANCE = new EnvironmentProvider();

        private SingletonHolder() {
            //empty
        }
    }

}

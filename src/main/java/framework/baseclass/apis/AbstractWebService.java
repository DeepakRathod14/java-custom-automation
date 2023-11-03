package framework.baseclass.apis;

import org.apache.commons.lang3.StringUtils;

import framework.baseclass.EnvironmentProvider;
import framework.baseclass.RestAssuredConfigurator;
import framework.bean.apiconfig.ApiConfig;
import framework.utilities.objectmapper.DtoConvert;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

/**
 * Maven property read environment decided
 * environment.json load > dto convert 
 * Default Request Specification set 
 * @author Deepak.Rathod
 *
 */
public abstract class AbstractWebService {

    protected String apiSetting;
    protected ApiConfig apiConfig;
    protected boolean useSwagger;
    
	protected AbstractWebService(String apiSetting, boolean useSwagger) {
		this.useSwagger = useSwagger;
		this.apiSetting = apiSetting;
		loadApiConfig();
        initEndPoints();
	}
	
    protected AbstractWebService(ApiConfig apiConfig, String apiSetting, boolean useSwagger) {
        this.useSwagger = useSwagger;
        this.apiConfig = apiConfig;
        this.apiSetting = apiSetting;
        loadApiConfig();
        initEndPoints();
    }
    
    protected abstract void initEndPoints();


    private void loadApiConfig() {
        Map<String, Object> mapping = EnvironmentProvider
            .provideEnvironment().getSettings(apiSetting);
        apiConfig = DtoConvert.mapToDto(ApiConfig.class, mapping, false);
    }
    
    protected RequestSpecification getDefaultSpecification() {
        RestAssuredConfigurator.Builder configurator = RestAssuredConfigurator.newBuilder()
            .usingContentType(ContentType.JSON);
        if (!StringUtils.isEmpty(apiConfig.getApiVersion())) {
            configurator.withConnectionInfo(apiConfig.getApiUrl(),
                apiConfig.getApiPort(),
                apiConfig.getApiBasePath(),
                apiConfig.getApiVersion());
        } else {
            configurator.withConnectionInfo(apiConfig.getApiUrl(),
                apiConfig.getApiPort(),
                apiConfig.getApiBasePath());
        }

        if (apiConfig.getDefaultHeaders() != null) {
            configurator.withHeaders(apiConfig.getDefaultHeaders());
        }
        return configurator.build().getRequestSpecification();
    }

    protected RequestSpecification buildUnversionedSpecs() {
        return RestAssuredConfigurator.newBuilder()
            .withConnectionInfo(apiConfig.getApiUrl(),
                apiConfig.getApiPort(),
                apiConfig.getApiBasePath())
            .usingContentType(ContentType.JSON)
            .withHeaders(apiConfig.getDefaultHeaders())
            .build().getRequestSpecification();
    }
}

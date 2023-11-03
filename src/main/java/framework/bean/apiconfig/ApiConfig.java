package framework.bean.apiconfig;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiConfig {

    @JsonProperty(value = "ApiURL", access = JsonProperty.Access.READ_WRITE)
    private String apiUrl;

    @JsonProperty(value = "ApiNodes", access = JsonProperty.Access.READ_WRITE)
    private List<String> apiNodes;

    @JsonProperty(value = "RedirectURL", access = JsonProperty.Access.READ_WRITE)
    private String redirectUrl;

    @JsonProperty(value = "RetryHosts", access = JsonProperty.Access.READ_WRITE)
    private List<String> retryHosts;

    @JsonProperty(value = "ApiPort", access = JsonProperty.Access.READ_WRITE)
    private Integer apiPort;
    @JsonProperty(value = "ApiBasePath", access = JsonProperty.Access.READ_WRITE)
    private String apiBasePath;
    @JsonProperty(value = "ApiVersion", access = JsonProperty.Access.READ_WRITE)
    private String apiVersion;
    @JsonProperty(value = "SwaggerDocumentation", access = JsonProperty.Access.READ_WRITE)
    private String swaggerDocumentation;

    @JsonProperty(value = "DefaultHeaders", access = JsonProperty.Access.READ_WRITE)
    private Map<String, String> defaultHeaders;

    @JsonProperty(value = "Cassandra", access = JsonProperty.Access.READ_WRITE)
    private ConnectionSettings cassandra;

    @JsonProperty(value = "SSHSettings", access = JsonProperty.Access.READ_WRITE)
    private ConnectionInfo connectionInfo;

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public ApiConfig setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
        return this;
    }

    public List<String> getRetryHosts() {
        return retryHosts;
    }

    public ApiConfig setRetryHosts(List<String> retryHosts) {
        this.retryHosts = retryHosts;
        return this;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public ApiConfig setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public Integer getApiPort() {
        return apiPort;
    }

    public ApiConfig setApiPort(Integer apiPort) {
        this.apiPort = apiPort;
        return this;
    }

    public String getApiBasePath() {
        return apiBasePath;
    }

    public ApiConfig setApiBasePath(String apiBasePath) {
        this.apiBasePath = apiBasePath;
        return this;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public ApiConfig setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public ApiConfig setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
        return this;
    }

    public String getSwaggerDocumentation() {
        return swaggerDocumentation;
    }

    public ApiConfig setSwaggerDocumentation(String swaggerDocumentation) {
        this.swaggerDocumentation = swaggerDocumentation;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public ApiConfig setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public List<String> getApiNodes() {
        return apiNodes;
    }

    public ApiConfig setApiNodes(List<String> apiNodes) {
        this.apiNodes = apiNodes;
        return this;
    }

    public ConnectionSettings getCassandra() {
        return cassandra;
    }

    public void setCassandra(ConnectionSettings cassandra) {
        this.cassandra = cassandra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiConfig apiConfig = (ApiConfig) o;
        return Objects.equals(apiUrl, apiConfig.apiUrl)
            && Objects.equals(apiNodes, apiConfig.apiNodes)
            && Objects.equals(redirectUrl, apiConfig.redirectUrl)
            && Objects.equals(retryHosts, apiConfig.retryHosts)
            && Objects.equals(apiPort, apiConfig.apiPort)
            && Objects.equals(apiBasePath, apiConfig.apiBasePath)
            && Objects.equals(apiVersion, apiConfig.apiVersion)
            && Objects.equals(swaggerDocumentation, apiConfig.swaggerDocumentation)
            && Objects.equals(defaultHeaders, apiConfig.defaultHeaders)
            && Objects.equals(cassandra, apiConfig.cassandra)
            &&  Objects.equals(connectionInfo, apiConfig.connectionInfo);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(apiUrl, apiNodes, redirectUrl, retryHosts, apiPort, apiBasePath, apiVersion, swaggerDocumentation,
                defaultHeaders, cassandra, connectionInfo);
    }
}

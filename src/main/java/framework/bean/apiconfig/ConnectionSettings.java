package framework.bean.apiconfig;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectionSettings {

    @JsonProperty(value = "CassandraNode", access = JsonProperty.Access.READ_WRITE)
    private String cassandraNode;
    @JsonProperty(value = "CassandraPort", access = JsonProperty.Access.READ_WRITE)
    private Integer cassandraPort;
    @JsonProperty(value = "ContactPoints", access = JsonProperty.Access.READ_WRITE)
    private List<String> contactPoints;
    @JsonProperty(value = "SSHSettings", access = JsonProperty.Access.READ_WRITE)
    private ConnectionInfo connectionInfo;
    @JsonProperty(value = "ReadTimeOutMillis", access = JsonProperty.Access.READ_WRITE)
    private Integer readTimeOutMillis;
    @JsonProperty(value = "ConnectionTimeOutMillis", access = JsonProperty.Access.READ_WRITE)
    private Integer connectionTimeOutMillis;

    @JsonIgnore
    public Map<Object, Object> unknownFields = new HashMap<>();

    @JsonAnyGetter
    public Map<Object, Object> any() {
        return unknownFields;
    }

    @JsonAnySetter
    public void set(Object name, Object value) {
        unknownFields.put(name, value);
    }

    public String getCassandraNode() {
        return cassandraNode;
    }

    public void setCassandraNode(String cassandraNode) {
        this.cassandraNode = cassandraNode;
    }

    public Integer getCassandraPort() {
        return cassandraPort;
    }

    public void setCassandraPort(Integer cassandraPort) {
        this.cassandraPort = cassandraPort;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public List<String> getContactPoints() {
        return contactPoints;
    }

    public ConnectionSettings setContactPoints(List<String> contactPoints) {
        this.contactPoints = contactPoints;
        return this;
    }

    public Integer getReadTimeOutMillis() {
        return readTimeOutMillis;
    }

    public ConnectionSettings setReadTimeOutMillis(Integer readTimeOutMillis) {
        this.readTimeOutMillis = readTimeOutMillis;
        return this;
    }

    public Integer getConnectionTimeOutMillis() {
        return connectionTimeOutMillis;
    }

    public ConnectionSettings setConnectionTimeOutMillis(Integer connectionTimeOutMillis) {
        this.connectionTimeOutMillis = connectionTimeOutMillis;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConnectionSettings that = (ConnectionSettings) o;

        return new EqualsBuilder()
            .append(cassandraNode, that.cassandraNode)
            .append(cassandraPort, that.cassandraPort)
            .append(contactPoints, that.contactPoints)
            .append(connectionInfo, that.connectionInfo)
            .append(readTimeOutMillis, that.readTimeOutMillis)
            .append(connectionTimeOutMillis, that.connectionTimeOutMillis)
            .append(unknownFields, that.unknownFields)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(cassandraNode)
            .append(cassandraPort)
            .append(contactPoints)
            .append(connectionInfo)
            .append(readTimeOutMillis)
            .append(connectionTimeOutMillis)
            .append(unknownFields)
            .toHashCode();
    }
}

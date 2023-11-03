package framework.bean.apiconfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import framework.bean.AbstractDto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectionInfo extends AbstractDto{

    @JsonProperty(value = "Instance", access = JsonProperty.Access.READ_WRITE)
    private Integer instance;
    @JsonProperty(value = "RemoteAddress", access = JsonProperty.Access.READ_WRITE)
    private String remoteAddress;
    @JsonProperty(value = "RemotePort", access = JsonProperty.Access.READ_WRITE)
    private Integer remotePort;

    @JsonProperty(value = "UserName", access = JsonProperty.Access.READ_WRITE)
    private String userName;
    @JsonProperty(value = "UserPassword", access = JsonProperty.Access.READ_WRITE)
    private String userPassword;
    @JsonProperty(value = "IsUserInfoSkipped", access = JsonProperty.Access.READ_WRITE)
    private boolean userInfoSkipped; // esxi requires user info to be skipped for opening ssh session

    @JsonProperty(value = "TargetPort", access = JsonProperty.Access.READ_WRITE)
    private Integer targetPort;
    @JsonProperty(value = "LocalAddress", access = JsonProperty.Access.READ_WRITE)
    private String localAddress;
    @JsonProperty(value = "LocalPort", access = JsonProperty.Access.READ_WRITE)
    private Integer localPort;
    @JsonProperty(value = "PortLForwardingEnabled", access = JsonProperty.Access.READ_WRITE)
    private Boolean portLForwardingEnabled;
    @JsonProperty(value = "PortRForwardingEnabled", access = JsonProperty.Access.READ_WRITE)
    private Boolean portRForwardingEnabled;
    @JsonProperty(value = "DynamicPortForwarding", access = JsonProperty.Access.READ_WRITE)
    private Integer dynamicPortForwarding;
    @JsonProperty(value = "PrivateKeyLocation", access = JsonProperty.Access.READ_WRITE)
    private String privateKeyLocation;
    @JsonProperty(value = "domain", access = JsonProperty.Access.READ_WRITE)
    private String domain;
    @JsonProperty(value = "SSHExecutorType", access = JsonProperty.Access.READ_WRITE)
    private String sshExecutorType; // script command executor type for now supported two: JSCH and MINA executors

    public ConnectionInfo() {
        // default constructor
    }

    /**
     * Constructor to clone ConnectionInfo object.
     *
     * @param obj - ConnectionInfo object to clone
     */
    public ConnectionInfo(ConnectionInfo obj) {
        this.instance = obj.instance;
        this.remoteAddress = obj.remoteAddress;
        this.remotePort = obj.remotePort;
        this.userName = obj.userName;
        this.userPassword = obj.userPassword;
        this.userInfoSkipped = obj.userInfoSkipped;
        this.targetPort = obj.targetPort;
        this.localAddress = obj.localAddress;
        this.localPort = obj.localPort;
        this.portLForwardingEnabled = obj.portLForwardingEnabled;
        this.portRForwardingEnabled = obj.portRForwardingEnabled;
        this.dynamicPortForwarding = obj.dynamicPortForwarding;
        this.privateKeyLocation = obj.privateKeyLocation;
        this.domain = obj.domain;
        this.sshExecutorType = obj.sshExecutorType;
    }


    public boolean getUserInfoSkipped() {
        return userInfoSkipped;
    }

    public ConnectionInfo setUserInfoSkipped(boolean userInfoSkipped) {
        this.userInfoSkipped = userInfoSkipped;
        return this;
    }

    public Integer getInstance() {
        return instance;
    }

    public ConnectionInfo setInstance(Integer instance) {
        this.instance = instance;
        return this;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public ConnectionInfo setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public ConnectionInfo setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public ConnectionInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public ConnectionInfo setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        return this;
    }

    public Integer getTargetPort() {
        return targetPort;
    }

    public ConnectionInfo setTargetPort(Integer targetPort) {
        this.targetPort = targetPort;
        return this;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public ConnectionInfo setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
        return this;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public ConnectionInfo setLocalPort(Integer localPort) {
        this.localPort = localPort;
        return this;
    }

    public Boolean isPortLForwardingEnabled() {
        return portLForwardingEnabled;
    }

    public ConnectionInfo setPortLForwardingEnabled(Boolean portLForwardingEnabled) {
        this.portLForwardingEnabled = portLForwardingEnabled;
        return this;
    }

    public Integer getDynamicPortForwarding() {
        return dynamicPortForwarding;
    }

    public ConnectionInfo setDynamicPortForwarding(Integer dynamicPortForwarding) {
        this.dynamicPortForwarding = dynamicPortForwarding;
        return this;
    }

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }

    public ConnectionInfo setPrivateKeyLocation(String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public ConnectionInfo setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Boolean getPortRForwardingEnabled() {
        return portRForwardingEnabled;
    }

    public ConnectionInfo setPortRForwardingEnabled(Boolean portRForwardingEnabled) {
        this.portRForwardingEnabled = portRForwardingEnabled;
        return this;
    }

    public String getSshExecutorType() {
        return sshExecutorType;
    }

    @JsonIgnore
    public SshExecutorType getSshExecutorTypeEnum() {
        return SshExecutorType.getEnum(sshExecutorType);
    }

    private ConnectionInfo setSshExecutorType(String sshExecutorType) {
        this.sshExecutorType = sshExecutorType;
        return this;
    }

    @JsonIgnore
    public ConnectionInfo setSshExecutorType(SshExecutorType sshExecutorType) {
        setSshExecutorType(sshExecutorType.getValue());
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

        ConnectionInfo that = (ConnectionInfo) o;

        return new EqualsBuilder()
            .appendSuper(super.equals(o))
            .append(userInfoSkipped, that.userInfoSkipped)
            .append(instance, that.instance)
            .append(remoteAddress, that.remoteAddress)
            .append(remotePort, that.remotePort)
            .append(userName, that.userName)
            .append(userPassword, that.userPassword)
            .append(targetPort, that.targetPort)
            .append(localAddress, that.localAddress)
            .append(localPort, that.localPort)
            .append(portLForwardingEnabled, that.portLForwardingEnabled)
            .append(portRForwardingEnabled, that.portRForwardingEnabled)
            .append(dynamicPortForwarding, that.dynamicPortForwarding)
            .append(privateKeyLocation, that.privateKeyLocation)
            .append(domain, that.domain)
            .append(sshExecutorType, that.sshExecutorType)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .appendSuper(super.hashCode())
            .append(instance)
            .append(remoteAddress)
            .append(remotePort)
            .append(userName)
            .append(userPassword)
            .append(targetPort)
            .append(localAddress)
            .append(localPort)
            .append(portLForwardingEnabled)
            .append(portRForwardingEnabled)
            .append(dynamicPortForwarding)
            .append(privateKeyLocation)
            .append(domain)
            .append(sshExecutorType)
            .append(userInfoSkipped)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "ConnectionInfo: " + toPrettyJsonString();
    }

}

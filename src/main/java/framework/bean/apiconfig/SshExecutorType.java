package framework.bean.apiconfig;

import java.util.stream.Stream;

public enum SshExecutorType {

    MINA("MINA"),
    JSCH("JSCH"),
    UNKNOWN("UNKNOWN");

    private final String value;

    SshExecutorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Locate SSHExecutorType by string name in this enum.
     * @param value String
     * @return SSHExecutorType
     */
    public static SshExecutorType getEnum(String value) {
        return Stream.of(SshExecutorType.values())
            .filter(executorType -> executorType.getValue().equalsIgnoreCase(value))
            .findFirst()
            .orElse(SshExecutorType.UNKNOWN);
    }


}

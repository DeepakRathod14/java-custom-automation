package framework.utilities.timestampbuilder;

public final class TimeConstants {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIMESTAMP_TIMEZONE_FORMAT = "yyyy-MM-dd HH:mm:ss Z z";
    public static final String RUN_TIME_TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String TIMESTAMP_PATTERN_NO_ZONE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String API_TIMESTAMP_PATTERN = "yyyy-MM-dd";
    public static final String DB_TIMESTAMP_PATTERN = "yyyyMMdd";
    public static final String NULL_TIME = "0001-01-01T00:00:00Z";
    public static final String NULL_RUN_TIME = "1970-01-01T00:00:00Z";
    // 'a' can be provided from WebUI as am or pm instead of AM, PM. Transform to uppercase first or will fail.
    public static final String TIME_PICKER_PATTERN = "h:mm a";
    public static final String DATE_WITHOUT_TIME_PATTERN = "MMM dd yyyy";
    public static final String LEGACY_DATA_PATTERN = "M'/'d'/'yyyy h:mm:ss a";
    public static final String SUMMARY_TABLE_PATTERN = "E, MMM d', 'yyyy' at 'h:mm a";
    public static final String UI_DEVICES_TABLE_PATTERN = "MMM d', 'h:mm a";
    public static final String LAST_RUN_PATTERN = "MMM dd yyyy h:mm a";
    public static final String RECENT_PANEL_PATTERN = "E, MMM d', 'yyyy' at 'h:mm a z";
    public static final String UI_DATE_PICKER_PATTERN = "MM/dd/yyyy";
    public static final String GRAPHQL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String UI_DATE_WITHOUT_TIME_PATTERN = "MMM d', 'yyyy";
    public static final String UI_OS_PATCHING_DEVICE_SUMMARY_PATTERN = "MMM d', 'yyyy' 'h:mm a";
    public static final String UI_CUSTOM_FIELDS_DATETIME_PATTERN = "MMM d, yyyy h:mm a";
    public static final String ON_DEMAND_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'";
    public static final String UI_TICKETS_OBSERVED_DATE_PATTERN = "Y-M-d'T'H:m:s.Z";
    public static final String DEPLOYMENT_PACKAGE_TABLE_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    private TimeConstants() {
    }

}

package framework.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import framework.utilities.timestampbuilder.TimeConstants;

public class TimeHelper {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SIMPLE_FORMAT_TO_SECONDS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String GRAPHQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z z";

    private TimeHelper() {
        //default constructor
    }

    public static int getCurrentTimeSeconds() {
        long timeMillis = System.currentTimeMillis();
        return (int) TimeUnit.MILLISECONDS.toSeconds(timeMillis);
    }

    /**
     * Check if date is in utc format.
     *
     * @param dateTime input date time
     * @return true if format is correct
     */
    public static boolean isDateTimeUtc(String dateTime) {
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        ZonedDateTime zdateTime = ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        TimeZone providedZone = TimeZone.getTimeZone(zdateTime.getZone());
        LOGGER.debug("Verify that date time is UTC with provided date time: [{}]", providedZone.getID());
        return utcZone.equals(providedZone);
    }

    /**
     * Convert epoch long.
     *
     * @param timeStamp epoch
     * @param format desired format
     * @return String representation of epoch in desired format
     */
    public static String asUtc(Long timeStamp, String format) {
        DateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateFormatDb = Date.from(Instant.ofEpochSecond(timeStamp));
        return sdf.format(dateFormatDb);
    }

    /**
     * Convert epoch long.
     * @param timeStamp epoch
     * @return String representation of epoch in following format: yyyy-MM-dd'T'HH:mm:ss
     */
    public static String asUtc(Long timeStamp) {
        return asUtc(timeStamp, SIMPLE_FORMAT_TO_SECONDS);
    }

    /**
     * Convert String to date time to epoch.
     *
     * @param dateTime String date time in format '2019-07-05T07:54:00Z'
     * @return epoch in seconds. Example: 1562313240
     */
    public static Long getEpochSecond(String dateTime) {
        return Objects.requireNonNull(convertStringToDateTime(dateTime)).toInstant().getEpochSecond();
    }

    /**
     * Convert Epoch to String.
     *
     * @param epoch Long in format 1562313240
     * @return String date time. Example: '2019-07-05T07:54:00Z'
     */
    public static String convertEpochToDateTimeAsString(Long epoch) {
        Instant instant = Instant.ofEpochSecond(epoch);
        return instant.toString();
    }

    /**
     * Convert Epoch to String with given time zone.
     *
     * @param epoch Long in format 1562313240
     * @return String date time. Example: '2019-07-05T07:54:00Z'
     */
    public static String convertEpochToDateTimeAsString(Long epoch, ZoneOffset zoneOffset) {
        Instant instant = Instant.ofEpochSecond(epoch);
        return ZonedDateTime.ofInstant(instant, zoneOffset).toInstant().toString();
    }

    /**
     * Retrieve seconds from Now.
     *
     * @param timeStamp time stamp
     * @return int seconds
     */
    public static int getSecondsFromNowTo(Timestamp timeStamp) {
        return (int) timeStamp.getTime() - getCurrentTimeSeconds();
    }

    /**
     * Retrieve seconds from Now.
     *
     * @param timeStamp time stamp
     * @return int seconds
     */
    public static int getSecondsFromNowTo(String timeStamp) {
        return Integer.parseInt(timeStamp) - getCurrentTimeSeconds();
    }

    /**
     * Convert String to date time in simple format.
     */
    public static Date convertStringToDateTime(String dateTime) {
        return convertStringToDateTime(dateTime, SIMPLE_FORMAT_TO_SECONDS);
    }

    /**
     * Convert DateTime in String using format to Date.
     * Example:
     * convertStringToDateTime("2020-07-03 18:56:26.025 +0000 UTC", "yyyy-MM-dd HH:mm:ss")
     * output: Fri Jul 03 21:56:26 EEST 2020
     * @param dateTime input date
     * @param format date time format
     * @return Date
     */
    public static Date convertStringToDateTime(String dateTime, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(dateTime);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return parsedDate;
    }

    /**
     * Trim seconds and offsets.
     *
     * @param dateTime input date time
     * @return String representation with trimmed seconds and offset
     */
    public static String trimSecondsAndOffset(String dateTime) {
        if (dateTime.contains("+")) {
            dateTime = dateTime.substring(0, dateTime.indexOf("+"));
        }
        return dateTime.substring(0, dateTime.lastIndexOf(":")) + ":00Z";
    }


    /**
     * Trim offset.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30+01:00'
     * @return String date time in format '2011-12-03T10:15:30'
     */
    public static String trimTimeZone(String dateTime) {
        ZonedDateTime parsed = ZonedDateTime.parse(dateTime);
        return parsed
            .withZoneSameLocal(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Trim offset and add it to time part.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30+01:00'
     * @return String date time in format '2011-12-03T09:15:30Z'
     */
    public static String timeZoneToUtc(String dateTime) {
        ZonedDateTime parsed = ZonedDateTime.parse(dateTime);
        return parsed
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Convert time with local time zone to UTC.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30Z'
     * @return String date time in format '2011-12-03T09:15:30'
     */
    public static String localTimeToUtc(String dateTime) {
        String localDateTime = dateTime.replace("Z", "") + OffsetDateTime.now().getOffset().toString();
        ZonedDateTime parsed = ZonedDateTime.parse(localDateTime);
        return parsed
            .withZoneSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Convert utc time to local time.
     *
     * @param dateTime String date time in format '2019-10-11T01:45:00.000Z'
     * @return String date time in format '2019-10-11T04:45' if current timezone +3:00
     */
    public static LocalDateTime utcToJvmTime(String dateTime) {
        String localDateTime = dateTime.replace("Z", "");
        return LocalDateTime.parse(localDateTime)
            .plusSeconds(ZonedDateTime.now().getOffset().getTotalSeconds());
    }

    /**
     * Convert time with local time zone to UTC.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30Z'
     * @param timeZone Time zone to which convert UTC time
     * @return String date time in format '2011-12-03T11:15:30+01:00'
     */
    public static String addOffset(String dateTime, TimeZone timeZone) {
        return dateTime.replace("Z", "") + OffsetDateTime.now(timeZone.toZoneId()).getOffset().toString();
    }

    /**
     * Convert Utc time to specific zone.
     *
     * @param dateTime date time
     * @param timeZone input time zone
     * @return String representation of modified time
     */
    public static String utcTimeToZonedTime(String dateTime, TimeZone timeZone) {
        LocalDateTime time = LocalDateTime.parse(dateTime,
            DateTimeFormatter.ofPattern(TimeConstants.RUN_TIME_TIMESTAMP_PATTERN))
            .plusHours(timeZone.getRawOffset() / 3600000);
        return time.format(DateTimeFormatter.ofPattern(TimeConstants.RUN_TIME_TIMESTAMP_PATTERN)).replace("Z", "")
            + OffsetDateTime
            .now(timeZone.toZoneId()).getOffset().toString();
    }

    /**
     * Trim milliseconds from date time but leave in place time zone offset if present.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30.000'.
     *     Example: 2011-12-03T10:15:30.000Z', 2011-12-03T10:15:30.000+01:00'
     *
     * @return String date time in format '2011-12-03T10:15:30', '2011-12-03T10:15:30Z', '2011-12-03T10:15:30+01:00'
     */
    public static String trimMiliseconds(String dateTime) {
        boolean utc = false;
        String offset = "";
        if (dateTime.contains("Z")) {
            utc = true;
        }
        if (StringUtils.countMatches(dateTime, "-") > 2) {
            offset = dateTime.substring(dateTime.lastIndexOf("-"));
        }
        if (dateTime.contains("+")) {
            offset = dateTime.substring(dateTime.indexOf("+"));
        }
        if (dateTime.contains(".")) {
            dateTime = dateTime.substring(0, dateTime.indexOf("."));
            dateTime = dateTime + offset;
            dateTime = utc ? dateTime + "Z" : dateTime;
        }
        return dateTime;
    }

    /**
     * Trim seconds from date time but leave in place time zone offset if present.
     *
     * @param dateTime String date time in format '2011-12-03T10:15:30'.
     *     Examples: 2011-12-03T10:15:30Z','2011-12-03T10:15:30+01:00'
     *
     * @return String date time in format '2011-12-03T10:15:00', '2011-12-03T10:15:00Z', '2011-12-03T10:15:00+01:00'
     */
    public static String trimSeconds(String dateTime) {
        boolean utc = false;
        String offset = "";
        if (dateTime.contains("Z")) {
            utc = true;
        }
        if (StringUtils.countMatches(dateTime, "-") > 2) {
            offset = dateTime.substring(dateTime.lastIndexOf("-"));
            dateTime = dateTime.substring(0, dateTime.lastIndexOf("-"));
        }
        if (dateTime.contains("+")) {
            offset = dateTime.substring(dateTime.indexOf("+"));
            dateTime = dateTime.substring(0, dateTime.lastIndexOf("+"));
        }
        dateTime = dateTime.substring(0, dateTime.lastIndexOf(":")) + ":00";
        dateTime = dateTime + offset;
        dateTime = utc ? dateTime + "Z" : dateTime;
        return dateTime;
    }

    /**
     * Calculate date with given timezone.
     *
     * @param date input date
     * @param timeZone preferred timezone
     * @return String representation of date with shifted offset timezone
     */
    public static String calculateDateWithProvidedTimeZone(String date, String timeZone) {
        if (timeZone != null && !timeZone.isEmpty()) {
            return TimeHelper.addOffset(date, TimeZone.getTimeZone(timeZone));
        } else {
            return date;
        }
    }


    /**
     * Parse DateTime to LocalDateTime.
     *
     * @param dateTime String date time in format '2019-07-05T07:54:00Z'
     * @return String date time. Example: '2019-07-05T07:54:00Z'
     */
    public static LocalDateTime parseTime(String dateTime, String format) {
        return LocalDateTime.parse(dateTime,
            DateTimeFormatter.ofPattern(format));
    }

    /**
     * Trim Date from string representing LocalDateTime.
     *
     * @param dateTime String date time in format '2019-07-05T07:54:00Z'
     * @return String time. Example: '07:54:00'
     */
    public static String trimDate(String dateTime) {
        return dateTime.substring(dateTime.indexOf("T") + 1).replace("Z", "");
    }

    /**
     * Trim Time from string representing LocalDateTime.
     *
     * @param dateTime String date time in format '2019-07-05T07:54:00Z'
     * @return String time. Example: '2019-07-05T00:00:00Z'
     */
    public static String trimTime(String dateTime) {
        return dateTime.replaceFirst("T.*Z", "T00:00:00Z");
    }

    /**
     * Parse DateTime to LocalDateTime.
     *
     * @param date String date time in format 'Oct 07 2019'
     * @return String date time. Example: '2019-10-07T00:00:00Z'
     */
    public static String parseDateToUtc(String date, String format) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format, Locale.ENGLISH))
            .atStartOfDay().toInstant(ZoneOffset.UTC).toString();
    }

    /**
     * Reformat string representation in one format to another.
     *
     * @param dateTime String date time
     * @param actualFormat format of input, e.g "yyyy-MM-dd'T'HH:mm:ss'Z'"
     * @param expectedFormat desiredFormat, e.g "h:mm a"
     * @return String date time. Example: '11:45 PM'
     */
    public static String reformatLocalDateTime(String dateTime, String actualFormat, String expectedFormat) {
        return parseTime(dateTime, actualFormat).format(
            DateTimeFormatter.ofPattern(expectedFormat, Locale.ENGLISH));
    }

    /**
     * Time at UI time pickers displayed with 15 minutes interval.
     * So we need round time to nearest 0, 15, 30, 45 minutes
     *
     * @param dateTime String localDateTime
     * @param expectedFormat String like "h:mm a"
     * @param timeInterval int, round limit
     * @return String UI date time. Example: '1:30 PM'
     */
    public static String roundToTimeInterval(String dateTime, String expectedFormat, int timeInterval) {
        LocalDateTime localDateTime = TimeHelper.parseTime(dateTime, TimeConstants.RUN_TIME_TIMESTAMP_PATTERN);
        return roundToTimeInterval(localDateTime, expectedFormat, timeInterval);
    }

    /**
     * Time at UI time pickers displayed with 15 minutes interval.
     * So we need round time to nearest 0, 15, 30, 45 minutes.
     *
     * @param localDateTime localDateTime
     * @param expectedFormat String like "h:mm a"
     * @param timeInterval int, round limit
     * @return String UI date time. Example: '1:30 PM'
     */
    public static String roundToTimeInterval(LocalDateTime localDateTime, String expectedFormat, int timeInterval) {
        LocalDateTime calculatedDateTime;
        if (localDateTime.getMinute() % timeInterval != 0) {
            calculatedDateTime =
                localDateTime.plusMinutes((long) timeInterval - localDateTime.getMinute() % timeInterval);
        } else {
            calculatedDateTime = localDateTime;
        }
        return calculatedDateTime.format(DateTimeFormatter.ofPattern(expectedFormat));
    }


    /**
     * Calculate if date falls on given day of week (dayOfWeek) and if it is then calculate what is the count of this
     * day of week in the month. E.g. 29/11/2019 is a 5th Friday in November
     *
     * @param date localDate
     * @param dayOfWeek day of week
     * @return Integer value indicating what is the count of given date. Return -1 if given date doesn't match given day
     *     of week
     */
    public static int dateIsNthWeekdayInMonth(LocalDate date, DayOfWeek dayOfWeek) {
        //Return 01 if provided date is not provided dayOfWeek
        int index = -1;
        if (date.isEqual(date.with(TemporalAdjusters.dayOfWeekInMonth(1, dayOfWeek)))) {
            index = 0;
        }
        if (date.isEqual(date.with(TemporalAdjusters.dayOfWeekInMonth(2, dayOfWeek)))) {
            index = 1;
        }
        if (date.isEqual(date.with(TemporalAdjusters.dayOfWeekInMonth(3, dayOfWeek)))) {
            index = 2;
        }
        if (date.isEqual(date.with(TemporalAdjusters.dayOfWeekInMonth(4, dayOfWeek)))) {
            index = 3;
        }
        if (date.isEqual(date.with(TemporalAdjusters.dayOfWeekInMonth(5, dayOfWeek)))) {
            index = 4;
        }
        return index;
    }

    /**
     * Retrieves unix timestamp X days older.
     *
     * @param days - number of days older from current moment
     * @return unix timestamp X days older
     */
    public static int asTimestampMinusDays(int days) {
        int currTimeSeconds = TimeHelper.getCurrentTimeSeconds();
        return currTimeSeconds - (days * 86400);
    }

    /**
     * Converts timestamp to GraphQL format.
     *
     * @return unix date in format "yyyy-MM-dd HH:mm:ss Z z"
     */
    public static String asGraphQlDate(int timestamp) {
        return TimeHelper.asUtc((long) timestamp, GRAPHQL_DATE_FORMAT);
    }
}

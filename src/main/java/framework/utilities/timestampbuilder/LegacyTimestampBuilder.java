package framework.utilities.timestampbuilder;

import static framework.utilities.timestampbuilder.TimeConstants.RUN_TIME_TIMESTAMP_PATTERN;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Function;

import framework.utilities.TimeHelper;

public final class LegacyTimestampBuilder<T> implements TimestampBuilder<T> {
    private final Calendar calendar = Calendar.getInstance();
    private final FieldSetter<T> timeFieldSetter;
    private final String pattern;
    private TimeZone outputTimeZone;

    private LegacyTimestampBuilder(FieldSetter<T> timeFieldSetter, String pattern) {
        this.timeFieldSetter = timeFieldSetter;
        this.pattern = pattern;
    }

    @Override
    public TimestampBuilder<T> set(int year, int month, int day) {
        calendar.set(year, month - 1, day);
        return this;
    }

    @Override
    public TimestampBuilder<T> set(int year, int month, int day, int hour, int minute) {
        calendar.set(year, month - 1, day, hour, minute);
        return this;
    }

    @Override
    public TimestampBuilder<T> set(int year, int month, int day, int hour, int minute, int second) {
        calendar.set(year, month - 1, day, hour, minute, second);
        return this;
    }

    @Override
    public TimestampBuilder<T> year(int year) {
        calendar.set(Calendar.YEAR, year);
        return this;
    }

    @Override
    public TimestampBuilder<T> month(int month) {
        calendar.set(Calendar.MONTH, month - 1);
        return this;
    }

    @Override
    public TimestampBuilder<T> day(int day) {
        calendar.set(Calendar.DATE, day);
        return this;
    }

    @Override
    public TimestampBuilder<T> hour(int hour) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return this;
    }

    @Override
    public TimestampBuilder<T> minute(int minute) {
        calendar.set(Calendar.MINUTE, minute);
        return this;
    }

    @Override
    public TimestampBuilder<T> second(int second) {
        calendar.set(Calendar.SECOND, second);
        return this;
    }

    @Override
    public TimestampBuilder<T> millisecond(int millisecond) {
        calendar.set(Calendar.MILLISECOND, millisecond);
        return this;
    }

    @Override
    public TimestampBuilder<T> addMonths(int months) {
        calendar.add(Calendar.MONTH, months);
        return this;
    }

    @Override
    public TimestampBuilder<T> addDays(int days) {
        calendar.add(Calendar.DATE, days);
        return this;
    }

    @Override
    public TimestampBuilder<T> addHours(int hours) {
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return this;
    }

    @Override
    public TimestampBuilder<T> addMinutes(int minutes) {
        calendar.add(Calendar.MINUTE, minutes);
        return this;
    }

    @Override
    public TimestampBuilder<T> addSeconds(int seconds) {
        calendar.add(Calendar.SECOND, seconds);
        return this;
    }

    @Override
    public TimestampBuilder<T> addMilliseconds(int milliseconds) {
        calendar.add(Calendar.MILLISECOND, milliseconds);
        return this;
    }

    @Override
    public T build() {
        return timeFieldSetter.setFieldValue(toTimestamp());
    }

    private String getPattern() {
        return pattern;
    }

    private TimeZone getOutputTimeZone() {
        return outputTimeZone != null ? outputTimeZone : calendar.getTimeZone();
    }

    @Override
    public TimestampBuilder<T> timezone(TimeZone timeZone) {
        calendar.setTimeZone(timeZone);
        return this;
    }

    @Override
    public TimestampBuilder<T> timezone(String id) {
        return timezone(TimeZone.getTimeZone(id));
    }

    @Override
    public TimestampBuilder<T> outputTimezone(TimeZone timeZone) {
        outputTimeZone = timeZone;
        return this;
    }

    @Override
    public TimestampBuilder<T> outputTimezone(String id) {
        return outputTimezone(TimeZone.getTimeZone(id));
    }

    private String toTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getPattern());
        simpleDateFormat.setTimeZone(getOutputTimeZone());

        return simpleDateFormat.format(calendar.getTime());
    }

    public static <T> TimestampBuilder<T> newInstance(Function<String, T> function, String pattern) {
        return new LegacyTimestampBuilder<>(new FunctionFieldSetter<>(function), pattern);
    }

    public static <T> TimestampBuilder<T> newInstance(T returnObject, Consumer<String> consumer, String pattern) {
        return new LegacyTimestampBuilder<>(new ConsumerFieldSetter<>(returnObject, consumer), pattern);
    }

    public static TimestampBuilder<String> newInstance(String pattern) {
        return new LegacyTimestampBuilder<>(NullFieldSetter.getInstance(), pattern);
    }

    /**
     * Convert dateTime String representation to TimestampBuilder object.
     *
     * @param dateTime input date time
     * @return TimestampBuilder&#60;String&#62;
     */
    public static TimestampBuilder<String> valueOf(String dateTime) {
        Date date = TimeHelper.convertStringToDateTime(dateTime);
        LegacyTimestampBuilder<String> builder = new LegacyTimestampBuilder<>(NullFieldSetter.getInstance(),
            RUN_TIME_TIMESTAMP_PATTERN);
        builder.utc();
        builder.calendar.setTime(date);
        return builder;
    }
}

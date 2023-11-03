package framework.utilities.timestampbuilder;

import java.util.TimeZone;

public interface TimestampBuilder<T> {

    TimestampBuilder<T> timezone(TimeZone timeZone);

    TimestampBuilder<T> timezone(String id);

    TimestampBuilder<T> outputTimezone(TimeZone timeZone);

    TimestampBuilder<T> outputTimezone(String id);

    default TimestampBuilder<T> utc() {
        return this.outputTimezone("UTC");
    }

    TimestampBuilder<T> set(int year, int month, int day);

    TimestampBuilder<T> set(int year, int month, int day, int hour, int minute);

    TimestampBuilder<T> set(int year, int month, int day, int hour, int minute, int second);

    TimestampBuilder<T> year(int year);

    TimestampBuilder<T> month(int month);

    TimestampBuilder<T> day(int day);

    TimestampBuilder<T> hour(int hour);

    TimestampBuilder<T> minute(int minute);

    TimestampBuilder<T> second(int second);

    TimestampBuilder<T> millisecond(int millisecond);

    TimestampBuilder<T> addMonths(int months);

    TimestampBuilder<T> addDays(int days);

    TimestampBuilder<T> addHours(int hours);

    TimestampBuilder<T> addMinutes(int minutes);

    TimestampBuilder<T> addSeconds(int seconds);

    TimestampBuilder<T> addMilliseconds(int milliseconds);

    T build();
}

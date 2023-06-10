package me.omega.omegalib.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for working with time-related operations.
 */
@UtilityClass
public class Time {

    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)\\s*(year|yr|y|month|mo|day|d|hour|hr|h" +
                                                                    "|minute|min|m|second|sec|s)s?");

    /**
     * @return the current Unix time in milliseconds
     */
    public static long nowMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @return the current Unix time in seconds
     */
    public static long nowSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * @return the current Unix time as an {@link Instant}
     */
    public static Instant now() {
        return Instant.now();
    }

    /**
     * Gets a {@link Duration} representing the given amount of time in the given unit.
     *
     * @param unit   the time unit
     * @param amount the amount of time
     * @return the duration
     */
    public static Duration duration(@NonNull TimeUnit unit, long amount) {
        return switch (unit) {
            case NANOSECONDS -> Duration.ofNanos(amount);
            case MICROSECONDS -> Duration.ofNanos(amount * 1000);
            case MILLISECONDS -> Duration.ofMillis(amount);
            case SECONDS -> Duration.ofSeconds(amount);
            case MINUTES -> Duration.ofMinutes(amount);
            case HOURS -> Duration.ofHours(amount);
            case DAYS -> Duration.ofDays(amount);
        };
    }

    /**
     * Parses the given duration string into a {@link java.time.Duration} object.
     *
     * @param durationString the duration string to parse
     * @return the parsed {@link java.time.Duration} object, or {@link Optional#empty()} if the duration string is
     * invalid
     */
    public static Optional<Duration> parse(String durationString) {
        try {
            return Optional.ofNullable(parseUnsafe(durationString));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Unsafely parses the given duration string into a {@link java.time.Duration} object.
     *
     * @param durationString the duration string to parse
     * @return the parsed {@link java.time.Duration} object
     * @throws IllegalArgumentException if the duration string is invalid
     */
    public static Duration parseUnsafe(String durationString) throws IllegalArgumentException {
        Matcher matcher = DURATION_PATTERN.matcher(durationString);
        if (matcher.find()) {
            long years = 0;
            long months = 0;
            long days = 0;
            long hours = 0;
            long minutes = 0;
            long seconds = 0;
            do {
                long value = Long.parseLong(matcher.group(1));
                String unit = matcher.group(2);
                switch (unit) {
                    case "year", "yr" -> years += value;
                    case "month", "mo" -> months += value;
                    case "day", "dy" -> days += value;
                    case "hour", "hr" -> hours += value;
                    case "minute", "min", "m" -> minutes += value;
                    case "second", "sec", "s" -> seconds += value;
                }
            } while (matcher.find());

            return Duration.of(years, ChronoUnit.YEARS)
                           .plus(months, ChronoUnit.MONTHS)
                           .plusDays(days)
                           .plusHours(hours)
                           .plusMinutes(minutes)
                           .plusSeconds(seconds);
        }

        try {
            return Duration.parse(durationString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid duration string: " + durationString, e);
        }
    }

    /**
     * Formats the given duration into a human-readable string.
     * <p>If the format type is {@link TimeFormatType#CONCISE}, the returned string will be in the format of "xd, xh,
     * xm, xs". If the format type is {@link TimeFormatType#FULL}, the returned string will be in the format of "x days,
     * x hours, x minutes, x seconds".</p>
     *
     * @param duration   the duration to format
     * @param formatType the format type
     * @param omitUnits  any units to omit from the resulting string
     * @return the formatted string
     */
    public static String format(Duration duration, TimeFormatType formatType, ChronoUnit... omitUnits) {
        if (duration == null || formatType == null) {
            throw new IllegalArgumentException("Duration and formatType cannot be null");
        }

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        Set<ChronoUnit> omittedUnits = new HashSet<>(Arrays.asList(omitUnits));

        StringBuilder builder = new StringBuilder();

        if (!omittedUnits.contains(ChronoUnit.DAYS) && days > 0) {
            builder.append(formatUnit(days, TimeUnit.DAYS, formatType)).append(", ");
        }
        if (!omittedUnits.contains(ChronoUnit.HOURS) && hours > 0) {
            builder.append(formatUnit(hours, TimeUnit.HOURS, formatType)).append(", ");
        }
        if (!omittedUnits.contains(ChronoUnit.MINUTES) && minutes > 0) {
            builder.append(formatUnit(minutes, TimeUnit.MINUTES, formatType)).append(", ");
        }
        if (!omittedUnits.contains(ChronoUnit.SECONDS)) {
            builder.append(formatUnit(seconds, TimeUnit.SECONDS, formatType));
        }

        return builder.toString();
    }

    private static String formatUnit(long amount, TimeUnit unit, TimeFormatType formatType) {
        String unitName = formatType == TimeFormatType.FULL ? unit.name().toLowerCase() :
                          unit.name().substring(0, 1).toLowerCase();
        return amount + unitName + (amount == 1 ? "" : "s");
    }

    public enum TimeFormatType {
        CONCISE,
        FULL
    }

}

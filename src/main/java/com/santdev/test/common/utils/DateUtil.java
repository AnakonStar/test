package com.santdev.test.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtil {

    private static final ZoneOffset ZONE = ZoneOffset.UTC;

    private DateUtil() {
    }

    public static String formatDate(Object value) {
        return format(value, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZONE));
    }

    public static String formatDateISO(Object value) {
        return format(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZONE));
    }

    public static String formatDateISOShort(Object value) {
        return format(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").withZone(ZONE));
    }

    public static String formatDateShort(Object value) {
        return format(value, DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZONE));
    }

    public static String formatDateCustom(Object value, String pattern) {
        return format(value, DateTimeFormatter.ofPattern(pattern).withZone(ZONE));
    }

    private static String format(Object value, DateTimeFormatter formatter) {
        Instant instant = toInstant(value);

        if (instant == null) {
            return null;
        }

        return formatter.format(instant);
    }

    private static Instant toInstant(Object value) {
        try {
            if (value == null || value instanceof Boolean) {
                return null;
            }

            if (value instanceof Instant instant) {
                return instant;
            }

            if (value instanceof Date date) {
                return date.toInstant();
            }

            if (value instanceof Number number) {
                return Instant.ofEpochMilli(number.longValue());
            }

            if (value instanceof CharSequence text) {
                return Instant.parse(text);
            }

            if (value instanceof LocalDateTime localDateTime) {
                return localDateTime.toInstant(ZONE);
            }

            if (value instanceof LocalDate localDate) {
                return localDate.atStartOfDay().toInstant(ZONE);
            }

            if (value instanceof TemporalAccessor temporalAccessor) {
                return Instant.from(temporalAccessor);
            }
        } catch (Exception exception) {
            return null;
        }

        return null;
    }
}

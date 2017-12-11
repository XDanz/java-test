package com.terraware;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Magnus Poromaa
 * @author Roger Forsberg
 */
public class DateUtil {

    public static String dateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        return format.format(date);
    }

    public static String dateToString(String formatAsString, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(formatAsString);
        return format.format(date);
    }

    /**
     * Converts a string on format "yyyyMMdd HH:mm:ss" or "yyyy-MM-dd HH:mm:ss" to a date
     *
     * @param str string that will be converted
     * @return a Date parsed from the string
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    public static Date parseDateTime(String str) {
        String format = "yyyyMMdd HH:mm:ss";
        if (str.contains("-"))
            format = "yyyy-MM-dd HH:mm:ss";
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Invalid date format on string %s. Expected yyyyMMdd HH:mm:ss or yyyy-MM-dd HH:mm:ss", str), e);
        }
    }

    /**
     * Converts a string on format "yyyyMMdd" or "yyyy-MM-dd" to a date
     *
     * @param str string that will be converted
     * @return a Date parsed from the string
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    public static Date parseDate(String str) {
        String format = "yyyyMMdd";
        if (str.contains("-"))
            format = "yyyy-MM-dd";
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Invalid date format on string %s. Expected yyyyMMdd or yyyy-MM-dd", str), e);
        }
    }

    /**
     * Converts a string on format "yyyy-MM-dd hh:mm:ss" to a Calendar
     *
     * @param str string that will be converted
     * @return a Calendar parsed from the string
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    public static Calendar parseCalendar(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dateInString = "2015-05-08 18:20:00";
        Date date;
        try {
            date = sdf.parse(dateInString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Invalid date format on string %s. Expected yyyy-MM-dd hh:mm:ss", str), e);
        }
    }

    /**
     * Converts a string with specified format
     *
     * @param str    string that will be converted
     * @param format format the string will be converted in
     * @return a Date parsed from the string
     * @throws IllegalArgumentException if the string cannot be parsed
     */
    public static Date parseDateTime(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            throw new IllegalArgumentException(String.format("Invalid date format on string %s but expected %s", str, format), e);
        }
    }

    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    public static LocalTime toLocalTime(Date date) {
        return toLocalDateTime(date).toLocalTime();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }
    
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static Date toDate(LocalDate localDate) {
        return toDate(LocalDateTime.of(localDate, LocalTime.MIDNIGHT));
    }

    public static Date toDate(LocalTime localTime) {
        Instant instant = localTime.atDate(LocalDate.of(1970, 1, 1)).
                atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date startOfDay(){
        return toDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date startOfDay(Date date) {
        Objects.requireNonNull(date, "date");
        return DateUtil.toDate(DateUtil.toLocalDateTime(date).withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public static Date endOfDay(Date date) {
        Objects.requireNonNull(date, "date");
        return DateUtil.toDate(DateUtil.toLocalDateTime(date).withHour(23).withMinute(59).withSecond(59).withNano(0));
    }
    
    public static Date endOfToday() {
        return DateUtil.toDate(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(0));
    }
}

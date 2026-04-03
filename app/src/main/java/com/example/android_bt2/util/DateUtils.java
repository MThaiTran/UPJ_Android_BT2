package com.example.android_bt2.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
    }

    public static String today() {
        return new SimpleDateFormat(DATE_PATTERN, Locale.US).format(new Date());
    }

    public static String now() {
        return new SimpleDateFormat(DATE_TIME_PATTERN, Locale.US).format(new Date());
    }
}


package com.getsetgo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utilities {

    public static String getCurrentDateTime() {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
                "KK:mm a", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        String currDate = DATE_TIME_FORMAT.format(cal
                .getTime());

        return currDate;
    }}

package com.getsetgo.util;

import android.view.View;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    public static String getCurrentDateTime() {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
                "KK:mm a", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        String currDate = DATE_TIME_FORMAT.format(cal
                .getTime());

        return currDate;
    }

    public static String getFormatDate() {
        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(dt);
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static String getShortMonthNames(String date) {

        try {
            if (date != null && !date.equalsIgnoreCase("null") && !date.isEmpty()) {

                String[] sp = date.split(" ");
                String month = sp[1];
                String shortMonth = null;
                String[] shortMonths = new DateFormatSymbols().getShortMonths();
                for (int i = 0; i < (shortMonths.length - 1); i++) {
                    if (shortMonths[i].equalsIgnoreCase(month.substring(0, 3))) {
                        shortMonth = shortMonths[i];
                    }
                }
                date = date.replace(month, shortMonth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String splitString(String date) {
        String[] sp = date.split(" ");
        return sp[1];
    }

    public static String setFirstWordCap(final EditText editText) {
        final String[] text = new String[1];

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    StringBuilder sb = new StringBuilder(editText.getText().toString());
                    if (sb.length() != 0) {
                        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                        editText.setText(sb);
                        text[0] = editText.getText().toString();
                    }
                }
            });
        return text[0];
    }

}

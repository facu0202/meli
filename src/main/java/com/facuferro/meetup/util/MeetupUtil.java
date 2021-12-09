package com.facuferro.meetup.util;

import java.util.Calendar;
import java.util.Date;

public class MeetupUtil {

    private MeetupUtil() {
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date addMontsToJavaUtilDate(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return new Date(calendar.getTimeInMillis());
    }
}

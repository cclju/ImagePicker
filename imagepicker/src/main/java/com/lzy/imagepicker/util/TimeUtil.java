package com.lzy.imagepicker.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jarvis on 2018/7/23.
 */

public class TimeUtil {

    public static final long NANOSECONDS_PER_SECOND = 1000000000L;
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long ONE_MONTH = 31L * 24L * 60L * 60L * 1000L;

    public static final long ONEDAY_IN_MILLISECOND = 86400000;
    public static final long THREE_DAY_IN_MILLISECOND = 259200000;
    public static final long ONEMONTH_MILL_TIME = ONEDAY_IN_MILLISECOND * 30;

    public static final int FIVE_MINUTES_IN_SECOND = 5 * 60;
    public static final int FIFTEEN_MINUTES_IN_SECOND = 15 * 60;

    public static final long ONE_HOUR= 60 * 60 * 1000L;
    public static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    public static final long ONE_WEEK=7L*ONE_DAY;
    public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000L;
    public static final long MILLISECONDS_PER_WEEK = 7 * 24 * 60 * 60 * 1000L;
    public static final long MILLISECONDS_IN_TWENTY_DAYS = 20 * 24 * 60 * 60 * 1000L;
    public static final long MILLISECONDS_IN_THIRTY_DAYS = 30 * 24 * 60 * 60 * 1000L;
    public static final int THIRTY_DAYS = 30;
    public static final int DAYS_IN_TWO_WEEKS = 14;
    public static final int DAYS_IN_ONE_WEEK = 7;

    public static boolean areSameDay(long timeStampA, long timeStampB) {
        String dateA = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStampA));
        String dateB = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStampB));
        return dateA.equals(dateB);
    }

    public static boolean isToday(long timeStamp) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timeStamp));
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return today.equals(date);
    }

    public static boolean isSatisfyCycleTime(long lastTimeSeconds, long currentTimeMilli, int cycleHours) {
        if (lastTimeSeconds > 0 && lastTimeSeconds > 0 && cycleHours > 0) {

            return (currentTimeMilli - lastTimeSeconds * MILLISECONDS_PER_SECOND) >= cycleHours * ONE_HOUR;
        }
        return true;
    }

    public static long getTimeSecondsBeforeDays(long time, int days) {
        if (days > 0 && time > 0) {

            return (time - (ONE_DAY  * days)) / MILLISECONDS_PER_SECOND;
        }

        return 0;
    }

    public static boolean isInMillsIntervalTime(long currentMillsTime, long beforeMillsTime,
                                                long millsInterval) {
        return  (currentMillsTime - beforeMillsTime) <= millsInterval;
    }

    public static String getDateYM(long timeStamp) {
        return new SimpleDateFormat("yyyy-MM").format(new Date(timeStamp));
    }

    public static boolean isSameWeek(long timeStamp1, long timeStamp2) {
        return isSameWeek(new Date(timeStamp1), new Date(timeStamp2));
    }

    public static boolean isSameWeek(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int subYear = calendar1.get(Calendar.YEAR)
                - calendar2.get(Calendar.YEAR);
        // subYear==0,说明是同一年
        if (subYear == 0) {
            if (calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        // 例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        // java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了

        // 说明:java的一月用"0"标识，那么12月用"11"
//        else if (subYear == 1 && calendar2.get(Calendar.MONTH) == 11) {
//            if (calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2
//                    .get(Calendar.WEEK_OF_YEAR))
//                return true;
//        }
        // 例子:cal1是"2004-12-31"，cal2是"2005-1-1"
//        else if (subYear == -1 && calendar1.get(Calendar.MONTH) == 11) {
//            if (calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2
//                    .get(Calendar.WEEK_OF_YEAR))
//                return true;
//
//        }
        return false;
    }

    public static int getGapCount(long oldTime, long newTime) {

        Date startDate = new Date();
        startDate.setTime(oldTime);

        Date endDate = new Date();
        endDate.setTime(newTime);

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }
}

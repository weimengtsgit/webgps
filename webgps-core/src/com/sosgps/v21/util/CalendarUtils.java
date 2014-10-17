package com.sosgps.v21.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {

    private static final long ONE_DAY = 86400000L;

    public static final String[] WEEKDAYS = new String[] { "UNDEFINED", "Sunday", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    private CalendarUtils() {
        // nothing to do
    }

    public static int getCurrentDayOfWeek() {
        return getCurrentDayOfWeekFor(new Date());
    }

    public static int getCurrentDayOfWeekFor(final Date date) {
        return getCalendarFor(date).get(Calendar.DAY_OF_WEEK);
    }

    public static Calendar getCalendarFor(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 返回相差秒数数
     * 
     * @param endDate
     * @param startDate
     * @return
     */
    public static int betweenSecond(Date endDate, Date startDate) {
        if (endDate == null || startDate == null) return 0;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(endDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(startDate);
        long l = c1.getTimeInMillis() - c2.getTimeInMillis();

        return (int) (l / (1000));
    }

    public static Date strToDate(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date strToDateTime(String str) {
        Date date = null;
        if (str != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = df.parse(str);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static Date strToDateTime(String timeStr, String pattern) {
        Date date = null;
        if (timeStr != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                date = df.parse(timeStr);
            } catch (ParseException e) {

            }
        }
        return date;
    }

    public static String dateTimeToStr(Date date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    public static String dateTimeToStr(Date date, String pattern) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    public static String dateToStr(Date date) {
        String str = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (date != null) {
            str = df.format(date);
        }
        return str;
    }

    /**
     * 在原有的日期上面加i天
     * 
     * @param date
     * @param i
     * @return
     */
    public static Date add(Date date, int i) {
        date = new Date(date.getTime() + i * ONE_DAY);
        return date;
    }

    /**
     * 加1天
     * 
     * @param date
     * @return
     */
    public static Date add(Date date) {
        return add(date, 1);
    }

    /**
     * 减1天
     * 
     * @param date
     * @return
     */
    public static Date sub(Date date) {
        return add(date, -1);
    }

    public static List<String> pickMinutesOfDate(Date startDate, Date endDate, long interval,
            String format) {
        if (startDate == null || endDate == null || interval == 0) return null;
        if (format == null || format.length() < 1) format = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        List<String> timeList = new ArrayList<String>();
        for (; startDate.getTime() <= endDate.getTime(); startDate.setTime(startDate.getTime()
                + interval)) {
            timeList.add(simpleDateFormat.format(startDate));
        }
        return timeList;
    }
    
    public static void main(String args[]){
    	System.out.println(getCurrentDayOfWeek());
    }
    
}

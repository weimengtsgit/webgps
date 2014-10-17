package com.sosgps.v21.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {

	/**
	 * 周模版
	 * 
	 * @param year
	 * @return
	 */
	public static List<Map<String, Object>> getWeekTemplate(int year) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int m = c.get(Calendar.DAY_OF_WEEK);
		if (m <= 2) {
			c.add(Calendar.DAY_OF_YEAR, 2 - m);
		} else {
			c.add(Calendar.DAY_OF_YEAR, 7 - m + 2);
		}

		int i = 1;
		while (true) {
			if (c.get(Calendar.YEAR) != year) {
				break;
			}

			item = new HashMap<String, Object>();

			Date startDay = c.getTime();

			Calendar temp = Calendar.getInstance();
			temp.set(Calendar.YEAR, year);
			temp.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR));
			temp.add(Calendar.DAY_OF_YEAR, 6);
			temp.set(Calendar.HOUR_OF_DAY, 23);
			temp.set(Calendar.MINUTE, 59);
			temp.set(Calendar.SECOND, 59);
			temp.set(Calendar.MILLISECOND, 999);

			Date endDay = temp.getTime();

			c.add(Calendar.DAY_OF_YEAR, 7);
			item.put("startDay", startDay);
			item.put("endDay", endDay);
			item.put("no", i);
			item.put("desc", "第" + i + "周("
					+ DateUtils.dateTimeToStr(startDay, "yyyy/MM/dd") + "-"
					+ DateUtils.dateTimeToStr(endDay, "yyyy/MM/dd") + ")");

			result.add(item);

			i++;
		}
		return result;
	}

	/**
	 * 周模版
	 * 
	 * @param year
	 * @return
	 */
	public static List<Map<String, Object>> getMonthTemplate(int year) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		for (int i = 1; i <= 12; i++) {
			item = new LinkedHashMap<String, Object>();

			Date startDay = c.getTime();

			Calendar temp = Calendar.getInstance();
			temp.set(Calendar.YEAR, year);
			temp.set(Calendar.MONTH, c.get(Calendar.MONTH));
			temp.add(Calendar.MONTH, 1);
			temp.set(Calendar.DAY_OF_MONTH, 1);
			temp.add(Calendar.DAY_OF_YEAR, -1);
			temp.set(Calendar.HOUR_OF_DAY, 23);
			temp.set(Calendar.MINUTE, 59);
			temp.set(Calendar.SECOND, 59);
			temp.set(Calendar.MILLISECOND, 999);

			Date endDay = temp.getTime();

			item.put("no", i);
			item.put("startDay", startDay);
			item.put("endDay", endDay);
			item.put("desc", i + "月");
			result.add(item);

			c.add(Calendar.MONTH, 1);

		}
		return result;
	}

	/**
	 * 周模版
	 * 
	 * @param year
	 * @return
	 */
	public static List<Map<String, Object>> getPeriodTemplate(int year) {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = null;

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		int index = 1;
		for (int i = 1; i <= 12; i++) {
			item = new LinkedHashMap<String, Object>();

			c.set(Calendar.DAY_OF_MONTH, 1);
			Date startDay = c.getTime();

			Calendar temp = Calendar.getInstance();
			temp.set(Calendar.YEAR, year);
			temp.set(Calendar.MONTH, c.get(Calendar.MONTH));
			temp.set(Calendar.DAY_OF_MONTH, 10);
			temp.set(Calendar.HOUR_OF_DAY, 23);
			temp.set(Calendar.MINUTE, 59);
			temp.set(Calendar.SECOND, 59);
			temp.set(Calendar.MILLISECOND, 999);

			Date endDay = temp.getTime();

			item.put("no", index++);
			item.put("startDay", startDay);
			item.put("endDay", endDay);
			item.put("desc", i + "月上旬");
			result.add(item);

			// 中旬
			c.set(Calendar.DAY_OF_MONTH, 11);
			startDay = c.getTime();

			temp.set(Calendar.DAY_OF_MONTH, 20);
			endDay = temp.getTime();

			item = new LinkedHashMap<String, Object>();
			item.put("no", index++);
			item.put("startDay", startDay);
			item.put("endDay", endDay);
			item.put("desc", i + "月中旬");
			result.add(item);

			// 下旬
			c.set(Calendar.DAY_OF_MONTH, 21);
			startDay = c.getTime();

			temp.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
			temp.set(Calendar.DAY_OF_MONTH, 1);
			temp.add(Calendar.DAY_OF_YEAR, -1);
			endDay = temp.getTime();

			item = new LinkedHashMap<String, Object>();
			item.put("no", index++);
			item.put("startDay", startDay);
			item.put("endDay", endDay);
			item.put("desc", i + "月下旬");
			result.add(item);

			c.add(Calendar.MONTH, 1);

		}
		return result;
	}

	public static List<Map<String, Object>> getTemplate(int type, int year) {
		switch (type) {
		case 0:
			return DateUtils.getWeekTemplate(year);
		case 1:
			return DateUtils.getPeriodTemplate(year);
		case 2:
			return DateUtils.getMonthTemplate(year);
		default:
			return null;
		}

	}

	/**
	 * 获取日期所在月的开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getStartTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 获取日期所在月的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getEndTimeOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	/**
	 * 获取日期所在天的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getStartTimeOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 获取日期所在天的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getEndTimeOfDay(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	/**
	 * 获取日期所在周的开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getStartTimeOfWeek(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
		//1=周日
		if (1 == dayWeek) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		//设置当前时间为周一
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * 获取日期所在周的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Long getEntTimeOfWeek(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		//设置当前时间为周一
		calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
		//设置当前时间为周日
		calendar.add(Calendar.DATE, 6);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime().getTime();
	}

	private static final long ONE_DAY = 24 * 60 * 60 * 1000;

	private static final String[] WEEK_CN_NAME = { "周一", "周二", "周三", "周四",
			"周五", "周六", "周日" };

	/**
	 * 获取当前日期字符串 格式为YYYY-MM-DD
	 * 
	 * @return java.lang.String
	 */
	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(new Date());
		return s;
	}

	public static String getCurrentYear() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String s = df.format(new Date());
		return s;
	}

	public static int getCurrentYear(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy");
		String s = df.format(d);
		int year = Integer.valueOf(s);
		return year;
	}

	public static String getCurrentMonth() {
		SimpleDateFormat df = new SimpleDateFormat("MM");
		String s = df.format(new Date());
		return s;
	}

	public static String getDayInMonth(String sDate) {
		Date date = strToDate(sDate);
		SimpleDateFormat df = new SimpleDateFormat("dd");
		String s = df.format(date);
		return s;
	}

	public static String getDayInWeek(String sDate) {
		Date date = strToDate(sDate);
		SimpleDateFormat df = new SimpleDateFormat("EEE");
		String s = df.format(date);
		return s;
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
	 * 转化成中文类型的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStrCh(Date date) {
		String str = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
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
	 * 在原有日期上加（减）l秒
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date add(Date date, long l) {
		return new Date(date.getTime() + l * 1000);
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

	public static Date addMonths(Date date, int amount) {
		return add(date, Calendar.MONTH, amount);
	}

	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = df.format(new Date());
		return s;
	}

	public static String getCurrentDateTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		String s = df.format(new Date());
		return s;
	}

	public static String getCurrentDateWeek() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd EEE");
		String s = df.format(new Date());
		return s;

	}

	/**
	 * 返回月份之间的差。
	 * 
	 * @param startYear
	 * @param startMonth
	 * @param endYear
	 * @param endMonth
	 * @return
	 */
	public static int compareMonth(String startYear, String startMonth,
			String endYear, String endMonth) {
		return (Integer.parseInt(endYear) - Integer.parseInt(startYear)) * 12
				+ (Integer.parseInt(endMonth) - Integer.parseInt(startMonth));

	}

	/**
	 * @param sDate
	 * @return
	 */
	public static String getYearMonth(String sDate) {
		Date date1 = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = null;
		try {
			date1 = df.parse(sDate);
			df.applyPattern("yyMM");
			s = df.format(date1);
		} catch (ParseException e) {
			return s;
		}
		return s;
	}

	/**
	 * @param date
	 * @return
	 */
	public static String getYearMonth(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyMM");
		String s = null;

		s = df.format(date);

		return s;

	}

	public static String getYearMonthDay(String sDate) {
		Date date1 = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = null;
		try {
			date1 = df.parse(sDate);
			df.applyPattern("yyMMdd");
			s = df.format(date1);
		} catch (ParseException e) {
			return s;
		}
		return s;
	}

	/**
	 * @param sDate
	 * @return
	 */
	public static String getMonth(String sDate) {
		Date date1 = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String s = null;
		try {
			date1 = df.parse(sDate);
			df.applyPattern("MM");
			s = df.format(date1);
		} catch (ParseException e) {
			return s;
		}
		return s;

	}

	/**
	 * @param sDate1
	 * @param sDate2
	 * @return
	 */
	public static int compareDate(String sDate1, String sDate2) {

		Date date1 = null;
		Date date2 = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			date1 = dateFormat.parse(sDate1);
			date2 = dateFormat.parse(sDate2);
		} catch (ParseException e) {

		}

		long dif = 0;
		if (date2.after(date1))
			dif = (date2.getTime() - date1.getTime()) / 1000 / 60 / 60 / 24;
		else
			dif = (date1.getTime() - date2.getTime()) / 1000 / 60 / 60 / 24;

		return (int) dif;
	}

	public static int getDate(String sDate, String sTag) {
		int iSecondMinusPos = sDate.lastIndexOf('-');
		if (sTag.equalsIgnoreCase("y")) {
			return Integer.parseInt(sDate.substring(0, 4));
		} else if (sTag.equalsIgnoreCase("m")) {
			return Integer.parseInt(sDate.substring(5, iSecondMinusPos));
		} else
			return Integer.parseInt(sDate.substring(iSecondMinusPos + 1));
	}

	/**
	 * 得到当前时间的时分秒。格式：hh:mm:ss。
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar CD = Calendar.getInstance();
		int HH = CD.get(Calendar.HOUR);
		int NN = CD.get(Calendar.MINUTE);
		int SS = CD.get(Calendar.SECOND);
		String time = String.valueOf(HH) + ":" + String.valueOf(NN) + ":"
				+ String.valueOf(SS);
		return time;
	}

	/**
	 * 返回相差分钟数
	 * 
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static int betweenMinute(Date endDate, Date startDate) {
		if (endDate == null || startDate == null)
			return 0;

		Calendar c1 = Calendar.getInstance();
		c1.setTime(endDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(startDate);
		long l = c1.getTimeInMillis() - c2.getTimeInMillis();

		return (int) (l / (1000 * 60));
	}

	/**
	 * 返回相差秒数数
	 * 
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static int betweenSecond(Date endDate, Date startDate) {
		if (endDate == null || startDate == null)
			return 0;

		Calendar c1 = Calendar.getInstance();
		c1.setTime(endDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(startDate);
		long l = c1.getTimeInMillis() - c2.getTimeInMillis();

		return (int) (l / (1000));
	}

	public static int getDateWeekNum(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	public static String getDateWeekCnName(int dateNum) {
		return WEEK_CN_NAME[dateNum];
	}

	/**
	 * 取得日期在今年属于第几周
	 * 
	 * @param d
	 * @return
	 */
	public static int getWeekNumInThisYear(Date d) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(d);
		int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
		d.setDate(1);
		d.setMonth(0);
		int day = d.getDay();
		if(day != 1){
			weekNum = weekNum - 1;
		}
		return weekNum;
	}

	/**
	 * 取得日期在今年属于第几月
	 * 
	 * @param d
	 * @return
	 */
	public static int getMonthNumInThisYear(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int monthNum = calendar.get(Calendar.MONTH) + 1;
		return monthNum;
	}

	/**
	 * 取得日期在今年属于第几旬
	 * 
	 * @param d
	 * @return
	 */
	public static int getXunNumInThisYear(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		int monthNum = calendar.get(Calendar.MONTH);
		int xunNum = monthNum * 3;
		int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayNum <= 10) {
			return xunNum + 1;
		} else if (dayNum >= 11 && dayNum <= 20) {
			return xunNum + 2;
		} else {
			return xunNum + 3;
		}
	}

	/**
	 * 根据目标模板类型获取当前日期在今年的第几个周/旬/月
	 * 
	 * @param d
	 * @param type
	 * @return
	 */
	public static int getTargetOnInThisYear(Date d, int type) {
		if (type == 0) {
			return getWeekNumInThisYear(d);
		} else if (type == 1) {
			return getXunNumInThisYear(d);
		} else if (type == 2) {
			return getMonthNumInThisYear(d);
		}
		return 0;
	}

	/**
	 * 根据目标模板类型取得开始时间
	 * 
	 * @param targetType
	 * @param d
	 * @return
	 */
	public static Long getStartTimeByTargetType(int targetType, Date d) {
		if (targetType == 0) {
			return getStartTimeOfWeek(d);
		} else if (targetType == 1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
			if (dayNum <= 10) {
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			} else if (dayNum >= 11 && dayNum <= 20) {
				calendar.set(Calendar.DAY_OF_MONTH, 11);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			} else {
				calendar.set(Calendar.DAY_OF_MONTH, 21);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			}
		} else if (targetType == 2) {
			return getStartTimeOfMonth(d);
		}
		return d.getTime();
	}

	/**
	 * 根据目标模板类型取得结束时间
	 * 
	 * @param targetType
	 * @param d
	 * @return
	 */
	public static Long getEndTimeByTargetType(int targetType, Date d) {
		if (targetType == 0) {
			return getEntTimeOfWeek(d);
		} else if (targetType == 1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
			if (dayNum <= 10) {
				calendar.set(Calendar.DAY_OF_MONTH, 10);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				return calendar.getTime().getTime();
			} else if (dayNum >= 11 && dayNum <= 20) {
				calendar.set(Calendar.DAY_OF_MONTH, 20);
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				return calendar.getTime().getTime();
			} else {
				return getEndTimeOfMonth(d);
			}
		} else if (targetType == 2) {
			return getEndTimeOfMonth(d);
		}
		return 0L;
	}

	public static Long getEndDayByTargetType(int targetType, Date d) {
		if (targetType == 0) {
			return getEntTimeOfWeek(d);
		} else if (targetType == 1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
			if (dayNum <= 10) {
				calendar.set(Calendar.DAY_OF_MONTH, 10);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			} else if (dayNum >= 11 && dayNum <= 20) {
				calendar.set(Calendar.DAY_OF_MONTH, 20);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			} else {
				return getEndTimeOfMonth(d);
			}
		} else if (targetType == 2) {
			return getEndTimeOfMonth(d);
		}
		return 0L;
	}

	/**
	 * 取得趋势图开始时间
	 * 
	 * @param targetType
	 * @param d
	 * @return
	 */
	public static Long getStartTimeByTargetTypeHis(int targetType, Date d) {
		Long startTime = 0L;
		if (targetType == 0) {
			int i = 11 * 7;
			d = DateUtils.add(d, -i);
			startTime = DateUtils.getStartTimeByTargetType(0, d);
		} else if (targetType == 1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			int dayNum = calendar.get(Calendar.DAY_OF_MONTH);
			if (dayNum <= 10) {
				d = DateUtils.addMonths(d, -4);
				d = new Date(DateUtils.getEndDayByTargetType(1, d));
				d = DateUtils.add(d, 1);
				startTime = d.getTime();
			} else if (dayNum >= 10 && dayNum <= 20) {
				d = DateUtils.addMonths(d, -4);
				d = new Date(DateUtils.getEndDayByTargetType(1, d));
				d = DateUtils.add(d, 1);
				startTime = d.getTime();
			} else {
				d = DateUtils.addMonths(d, -3);
				startTime = DateUtils.getStartTimeOfMonth(d);
			}
		} else if (targetType == 2) {
			d = DateUtils.addMonths(d, -11);
			startTime = DateUtils.getStartTimeOfMonth(d);
		}
		return startTime;
	}

	public static int getTargetOnFromNow(int type, int year) {
		List<Map<String, Object>> dateList = null;
		switch (type) {
		case 0:
			dateList = DateUtils.getWeekTemplate(year);
		case 1:
			dateList = DateUtils.getPeriodTemplate(year);
		case 2:
			dateList = DateUtils.getMonthTemplate(year);
		}
		if (dateList != null) {
			long currentTime = new Date().getTime();
			for (Map<String, Object> date : dateList) {
				Date startTime = (Date) date.get("startDay");
				// 过滤掉当前时间之前的数据
				if (currentTime >= startTime.getTime()) {
					continue;
				}
				return (Integer) date.get("no");
			}
		}
		return -1;
	}

	public static int getTargetOnFromNow(List<Map<String, Object>> dateList) {
		if (dateList != null) {
			long currentTime = new Date().getTime();
			for (Map<String, Object> date : dateList) {
				Date endTime = (Date) date.get("endDay");
				// 过滤掉当前时间之前的数据
				if (currentTime > endTime.getTime()) {
					continue;
				}
				return (Integer) date.get("no");
			}
		}
		return -1;
	}

	public static String dateTimeToStr(Long date, String pattern) {
		if (date == null) {
			return "";
		}
		String str = null;
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		if (date != null) {
			str = df.format(date);
		}
		return str;
	}
	
	
	public static Long startTimeByTargetOn(int targetTemplateType, int year, int targetOn){
		if (targetTemplateType == 0) {
			//周
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.WEEK_OF_YEAR, targetOn + 1);
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			//calendar.add(Calendar.DATE, 1);
			return calendar.getTime().getTime();
		} else if (targetTemplateType == 1) {
			//旬
			int mod = targetOn % 3;
			int m = targetOn / 3;
			if(mod == 0){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m - 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.add(Calendar.DATE, 20);
				return calendar.getTime().getTime();
			}else if(mod == 1){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar.getTime().getTime();
			}else if(mod == 2){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.add(Calendar.DATE, 10);
				return calendar.getTime().getTime();
			}
		} else if (targetTemplateType == 2) {
			//月
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, targetOn - 1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return calendar.getTime().getTime();
		}
		return 0L;
	}
	
	public static Long endTimeByTargetOn(int targetTemplateType, int year, int targetOn){
		if (targetTemplateType == 0) {
			//周
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.WEEK_OF_YEAR, targetOn + 2);
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			calendar.add(Calendar.DATE, -1);
			return calendar.getTime().getTime();
		} else if (targetTemplateType == 1) {
			//旬
			int mod = targetOn % 3;
			int m = targetOn / 3;
			if(mod == 0){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m - 1);
				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				return calendar.getTime().getTime();
			}else if(mod == 1){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m);
				calendar.set(Calendar.DAY_OF_MONTH, 1);  
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				calendar.add(Calendar.DATE, 9);
				return calendar.getTime().getTime();
			}else if(mod == 2){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, m);
				calendar.set(Calendar.DAY_OF_MONTH, 1);  
				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);
				calendar.set(Calendar.MILLISECOND, 999);
				calendar.add(Calendar.DATE, 19);
				return calendar.getTime().getTime();
			}
		} else if (targetTemplateType == 2) {
			//月
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, targetOn - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			return calendar.getTime().getTime();
		}
		return 0L;
	}

	public static int getMonthCountByStr(String source, String formatStr){
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatStr);
		    Date date = format.parse(source);
		    Calendar calendar = new GregorianCalendar();
		    calendar.setTime(date);
	    	return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}catch (ParseException e) {
		      e.printStackTrace();
	    }
		return 31;
	}
	
	public static void main(String args[]) {
		// System.out.println(DateUtils.getStartTimeByTargetType(1, new
		// Date()));
		// System.out.println(new Date().getTime());
		
		Date d = new Date(); 
		System.out.println(getTargetOnInThisYear(d, 0));
		/*Date endTime = new Date(DateUtils.getEndTimeByTargetType(0, d)); 
		int i = 11 * 7; 
		d = DateUtils.add(d, -i); 
		System.out.println(DateUtils.dateToStr(d));*/
		//Date startTime = new Date(DateUtils.getStartTimeByTargetType(2, d));
		//Date endTime = new Date(DateUtils.getEndTimeByTargetType(2, d));
		//System.out.println(DateUtils.dateToStr(startTime));
		//System.out.println(DateUtils.dateToStr(endTime));
		/*Long startTime = DateUtils.getStartTimeByTargetType(2, d);
		Long endTime = DateUtils.getEndTimeByTargetType(2, d);
		System.out.println(startTime);
		System.out.println(endTime);*/
		
		
		/*Date d = new Date();
		Long startTime = DateUtils.getStartTimeByTargetTypeHis(1, d);
		Long endTime = DateUtils.getEndTimeByTargetType(1, d);
		System.out.println(startTime);
		System.out.println(endTime);
		System.out.println(DateUtils.dateToStr(new Date(startTime)));
		System.out.println(DateUtils.dateToStr(new Date(endTime)));*/
		
		/*Long endTime = 0L;
		endTime = DateUtils.getEndTimeOfMonth(new Date());
		System.out.println(endTime);
		System.out.println(DateUtils.dateTimeToStr(new Date(endTime)));*/
		/*int targetOn = 23;
		Long startTime = DateUtils.startTimeByTargetOn(1, 2012, targetOn);
		System.out.println(DateUtils.dateTimeToStr(new Date(startTime)));
		Long endTime = DateUtils.endTimeByTargetOn(1, 2012, targetOn);
		System.out.println(DateUtils.dateTimeToStr(new Date(endTime)));*/
		//System.out.println(endTime);
		
		
	}

}

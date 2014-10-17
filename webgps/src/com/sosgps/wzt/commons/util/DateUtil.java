package com.sosgps.wzt.commons.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @Title: DateUtil.java
 * @Description: 处理时间常用方法
 * @Copyright:
 * @Date: 2008-8-16 上午11:38:14
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class DateUtil {
	public DateUtil() {
		
	}
	
	/**
	 * Function: 得到当前系统日期
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.DATE));
	}

	/**
	 * Function: 得到当前系统日期和时间
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDateTime() {
		Calendar c = Calendar.getInstance();
		DateFormat df = DateFormat.getDateTimeInstance();
		return df.format(c.getTime());
	}

	/**
	 * Function: 得到当前系统时间
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getTime() {
		Calendar c = Calendar.getInstance();
		DateFormat df = DateFormat.getTimeInstance();
		return df.format(c.getTime());
	}

	/**
	 * Function: 得到当前系统年
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getYear() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.YEAR));
	}

	/**
	 * Function: 得到当前系统月
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getMonth() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.MONTH) + 1);
	}

	/**
	 * Function: 得到当前系统日
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDay() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Function: 得到当前系统星期
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getWeek() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * Function: 得到当前系统前一月
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getMonthBefore() {
		Calendar c = Calendar.getInstance();
		int i = c.get(Calendar.MONTH) + 1;
		if (i == 1) {
			i = 12;
		} else {
			i--;
		}
		return Integer.toString(i);
	}

	/**
	 * Function: 得到系统当前日期的前一天
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDayBefore() {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		return Integer.toString(cd.get(Calendar.DATE));
	}

	/**
	 * Function: 得到系统当前时间的小时
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getHour() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.HOUR));
	}

	/**
	 * Function: 得到系统当前时间的分钟
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getMinute() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.MINUTE));
	}

	/**
	 * Function: 得到系统当前时间的秒数
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getSecend() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.SECOND));
	}

	/**
	 * Function: 得到指定日期之间的天数
	 * 
	 * @param:Date beginDate,Date endDate
	 * @return: int
	 */
	public static int getDayNum(Date beginDate, Date endDate) {
		long lTime = endDate.getTime() - beginDate.getTime();
		int iResult = (int) (lTime / (1000 * 60 * 60 * 24) + 0.5);
		return iResult;
	}
	
	/**
	 * 将字符串转换为时间.
	 * 
	 * @param str
	 *            代表时间的字符串.
	 * @return --转换后的时间.str格式不对返回null.
	 */
	public static final java.sql.Timestamp strToTime(String str) {
		if (str == null) {
			return null;
		}
		str = str.trim();
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date tDate = null;
		try {
			tDate = formatter.parse(str);
		} catch (Exception e) {
		}
		if (tDate == null && str.indexOf(' ') < 0) {
			formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			try {
				tDate = formatter.parse(str);
			} catch (Exception e) {
			}
		}
		if (tDate == null) {
			return null;
		}
		java.sql.Timestamp timeStame = new java.sql.Timestamp(tDate.getTime());
		return timeStame;
	}

	/**
	 * 将特定形式的字符串转换成日期 支持的格式有：yyyy-mm-dd, yyyy/mm/dd, yyyy.mm-dd
	 * 
	 * @param str
	 * @return
	 * @throws java.lang.Exception
	 */
	public static java.sql.Date strToDate(String str) throws Exception {
		if (str == null) {
			throw new Exception("非法日期格式");
		}

		String newStr = null;

		if (str.trim().indexOf(" ") != -1) {
			newStr = str.trim().substring(0, str.trim().indexOf(" "));
		} else {
			newStr = str.trim();
		}

		char[] c = { '-', '.', '/' };
		StringTokenizer d = null;

		for (int i = 0; i < c.length; i++) {
			if (newStr.indexOf(c[i]) != -1) {
				d = new StringTokenizer(newStr, String.valueOf(c[i]));

				break;
			}
		}

		if ((d == null) || (d.countTokens() < 3)) {
			throw new Exception("非法日期格式");
		}

		int yy = 0;
		int mm = 0;
		int dd = 0;

		try {
			yy = Integer.parseInt(d.nextToken());
			mm = Integer.parseInt(d.nextToken());
			dd = Integer.parseInt(d.nextToken());
		} catch (NumberFormatException ex1) {
			throw new Exception("非法日期格式");
		}
		DateFormat sdf = DateFormat.getDateInstance();

		return new java.sql.Date(sdf.parse(yy + "-" + mm + "-" + dd).getTime());
	}

	/**
	 * Function: 判断是否为闰年
	 * 
	 * @param: SourceData int
	 * @return: boolean true-是,false-不是
	 */
	public static boolean isLeapYear(int year) {
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Function: 判断数据是否合法日期 
	 * @param: sourceData String--年,月,日
	 * @return: Boolean true-是,false-不是
	 */
	public static boolean isDate(String year, String month, String day) {
		if (year.length() == 0 || month.length() == 0 || day.length() == 0) {
			return false;
		}
		try {
			int y, m, d;
			y = Integer.parseInt(year);
			m = Integer.parseInt(month);
			d = Integer.parseInt(day);

			if (y < 1900 || y > 4712) {
				return false;
			}

			if (m < 1 || m > 12) {
				return false;
			}

			if (d < 1 || m > 31) {
				return false;
			}

			switch (d) {
			case 2:
				if (isLeapYear(y)) {
					if (d > 29) {
						return false;
					}
				} else {
					if (d > 28) {
						return false;
					}
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if (d > 30) {
					return false;
				}
				break;
			default:
				return true;
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 转换UTC时间为yyyymmdd格式
	 * @param UTC
	 * @return
	 */
	public static String convertUTC(String UTC){
		StringBuffer sb=new StringBuffer();
		String year=UTC.substring(24);
		String mon=convertToMon(UTC.substring(4, 7));
		String day=UTC.substring(8,10);
		sb.append(year).append(mon).append(day);
		return sb.toString();
	}
	/**
	 * 转换为数字型月份
	 * @param mon
	 * @return
	 */
	private static String convertToMon(String mon){
		if(mon.equals("Jan"))
			return "01";
		if(mon.equals("Feb"))
			return "02";
		if(mon.equals("Mar"))
			return "03";
		if(mon.equals("Apr"))
			return "04";
		if(mon.equals("May"))
			return "05";
		if(mon.equals("Jun"))
			return "06";
		if(mon.equals("Jul"))
			return "07";
		if(mon.equals("Aug"))
			return "08";
		if(mon.equals("Sep"))
			return "09";
		if(mon.equals("Oct"))
			return "10";
		if(mon.equals("Nov"))
			return "11";
		if(mon.equals("Dec"))
			return "12";
		return null;
	}
}

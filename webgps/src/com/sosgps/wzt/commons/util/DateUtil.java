package com.sosgps.wzt.commons.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @Title: DateUtil.java
 * @Description: ����ʱ�䳣�÷���
 * @Copyright:
 * @Date: 2008-8-16 ����11:38:14
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yanglei
 * @version 1.0
 */
public class DateUtil {
	public DateUtil() {
		
	}
	
	/**
	 * Function: �õ���ǰϵͳ����
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.DATE));
	}

	/**
	 * Function: �õ���ǰϵͳ���ں�ʱ��
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
	 * Function: �õ���ǰϵͳʱ��
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
	 * Function: �õ���ǰϵͳ��
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getYear() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.YEAR));
	}

	/**
	 * Function: �õ���ǰϵͳ��
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getMonth() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.MONTH) + 1);
	}

	/**
	 * Function: �õ���ǰϵͳ��
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getDay() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Function: �õ���ǰϵͳ����
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getWeek() {
		Calendar c = Calendar.getInstance();
		return Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * Function: �õ���ǰϵͳǰһ��
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
	 * Function: �õ�ϵͳ��ǰ���ڵ�ǰһ��
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
	 * Function: �õ�ϵͳ��ǰʱ���Сʱ
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getHour() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.HOUR));
	}

	/**
	 * Function: �õ�ϵͳ��ǰʱ��ķ���
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getMinute() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.MINUTE));
	}

	/**
	 * Function: �õ�ϵͳ��ǰʱ�������
	 * 
	 * @param:
	 * @return: String
	 */
	public static String getSecend() {
		Calendar cd = Calendar.getInstance();
		return Integer.toString(cd.get(Calendar.SECOND));
	}

	/**
	 * Function: �õ�ָ������֮�������
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
	 * ���ַ���ת��Ϊʱ��.
	 * 
	 * @param str
	 *            ����ʱ����ַ���.
	 * @return --ת�����ʱ��.str��ʽ���Է���null.
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
	 * ���ض���ʽ���ַ���ת�������� ֧�ֵĸ�ʽ�У�yyyy-mm-dd, yyyy/mm/dd, yyyy.mm-dd
	 * 
	 * @param str
	 * @return
	 * @throws java.lang.Exception
	 */
	public static java.sql.Date strToDate(String str) throws Exception {
		if (str == null) {
			throw new Exception("�Ƿ����ڸ�ʽ");
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
			throw new Exception("�Ƿ����ڸ�ʽ");
		}

		int yy = 0;
		int mm = 0;
		int dd = 0;

		try {
			yy = Integer.parseInt(d.nextToken());
			mm = Integer.parseInt(d.nextToken());
			dd = Integer.parseInt(d.nextToken());
		} catch (NumberFormatException ex1) {
			throw new Exception("�Ƿ����ڸ�ʽ");
		}
		DateFormat sdf = DateFormat.getDateInstance();

		return new java.sql.Date(sdf.parse(yy + "-" + mm + "-" + dd).getTime());
	}

	/**
	 * Function: �ж��Ƿ�Ϊ����
	 * 
	 * @param: SourceData int
	 * @return: boolean true-��,false-����
	 */
	public static boolean isLeapYear(int year) {
		if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Function: �ж������Ƿ�Ϸ����� 
	 * @param: sourceData String--��,��,��
	 * @return: Boolean true-��,false-����
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
	 * ת��UTCʱ��Ϊyyyymmdd��ʽ
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
	 * ת��Ϊ�������·�
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

package com.sosgps.wzt.stat.alarmstat.util;

import java.util.List;

import com.sosgps.wzt.stat.alarmstat.bean.DayBean;
import com.sosgps.wzt.stat.alarmstat.bean.MonthBean;
import com.sosgps.wzt.stat.alarmstat.bean.YearBean;


public class DateTool {
	public static List getYearList(){
		List list  = new java.util.ArrayList();
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		int year = calendar.get(calendar.YEAR);
		int[] years = new int[6];
		for (int x = 0; x < 6; x++) {
			YearBean yearBean = new YearBean();
			yearBean.setId(year - 5 + x);
			yearBean.setStrYear(String.valueOf(year - 5 + x));
			list.add(yearBean);
		}
		return list;
	}
	public static List getDayList(){
		List list  = new java.util.ArrayList();
		for(int x=1 ; x<32; x++){
			DayBean dayBean = new DayBean();
			dayBean.setId(x);
			dayBean.setStrDay(String.valueOf(x));
			list.add(dayBean);
		}
		return list;
	}
	public static List getMonthList(){
		List list  = new java.util.ArrayList();
		for(int x=1 ; x<13; x++){
			MonthBean monthBean = new MonthBean();
			monthBean.setId(x);
			monthBean.setStrMonth(String.valueOf(x));
			list.add(monthBean);
		}
		return list;
	}
}

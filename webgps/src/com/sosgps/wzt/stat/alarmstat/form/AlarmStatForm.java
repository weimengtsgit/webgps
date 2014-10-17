package com.sosgps.wzt.stat.alarmstat.form;

import java.util.ArrayList;
import java.util.List;

import com.sosgps.wzt.stat.alarmstat.bean.AlarmTjBean;
import com.sosgps.wzt.system.form.BaseForm;

public class AlarmStatForm extends BaseForm {
	private List resultLocList = new java.util.ArrayList();
	private List yearList;
	private List monthList;
	private List dayList;
	private String empCode;
	private String deviceId;

	private String strYear;
	private String strMonth;
	private String strDay;

	private String strEndYear;
	private String strEndMonth;
	private String strEndDay;
	
	private String queryType;
	private String strStartDate;
	private String strEndDate;
	private String strYearId;
	private String strMonthId;
	private String strDayId;
	private String strEndYearId;
	private String strEndMonthId;
	private String strEndDayId;
	
	private String isImportExcel;
	
	private AlarmTjBean alarmTjBean;

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getStrYear() {
		return strYear;
	}

	public void setStrYear(String strYear) {
		this.strYear = strYear;
	}

	public String getStrMonth() {
		return strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	public String getStrDay() {
		return strDay;
	}

	public void setStrDay(String strDay) {
		this.strDay = strDay;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getStrStartDate() {
		return strStartDate;
	}

	public void setStrStartDate(String strStartDate) {
		this.strStartDate = strStartDate;
	}

	public String getStrEndDate() {
		return strEndDate;
	}

	public void setStrEndDate(String strEndDate) {
		this.strEndDate = strEndDate;
	}

	public List getYearList() {
		return yearList;
	}

	public void setYearList(List yearList) {
		this.yearList = yearList;
	}

	public List getMonthList() {
		return monthList;
	}

	public void setMonthList(List montnList) {
		this.monthList = montnList;
	}

	public List getDayList() {
		return dayList;
	}

	public void setDayList(List dayList) {
		this.dayList = dayList;
	}

	public String getStrYearId() {
		return strYearId;
	}

	public void setStrYearId(String strYearId) {
		this.strYearId = strYearId;
	}

	public String getStrMonthId() {
		return strMonthId;
	}

	public void setStrMonthId(String strMonthId) {
		this.strMonthId = strMonthId;
	}

	public String getStrDayId() {
		return strDayId;
	}

	public void setStrDayId(String strDayId) {
		this.strDayId = strDayId;
	}

	public AlarmTjBean getAlarmTjBean() {
		return alarmTjBean;
	}

	public void setAlarmTjBean(AlarmTjBean alarmTjBean) {
		this.alarmTjBean = alarmTjBean;
	}

	public List getResultLocList() {
		return resultLocList;
	}

	public void setResultLocList(List resultLocList) {
		this.resultLocList = resultLocList;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getStrEndYearId() {
		return strEndYearId;
	}

	public void setStrEndYearId(String strEndYearId) {
		this.strEndYearId = strEndYearId;
	}

	public String getStrEndMonthId() {
		return strEndMonthId;
	}

	public void setStrEndMonthId(String strEndMonthId) {
		this.strEndMonthId = strEndMonthId;
	}

	public String getStrEndDayId() {
		return strEndDayId;
	}

	public void setStrEndDayId(String strEndDayId) {
		this.strEndDayId = strEndDayId;
	}

	public String getStrEndYear() {
		return strEndYear;
	}

	public void setStrEndYear(String strEndYear) {
		this.strEndYear = strEndYear;
	}

	public String getStrEndMonth() {
		return strEndMonth;
	}

	public void setStrEndMonth(String strEndMonth) {
		this.strEndMonth = strEndMonth;
	}

	public String getStrEndDay() {
		return strEndDay;
	}

	public void setStrEndDay(String strEndDay) {
		this.strEndDay = strEndDay;
	}

	public String getIsImportExcel() {
		return isImportExcel;
	}

	public void setIsImportExcel(String isImportExcel) {
		this.isImportExcel = isImportExcel;
	}

}

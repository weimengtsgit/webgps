/**
 * 
 */
package com.sosgps.wzt.stat.funstat.form;

import java.util.List;

import com.sosgps.wzt.system.form.BaseForm;

/**
 * @author xiaojun.luan
 *
 */
public class TimeStatForm extends BaseForm {
	private List yearList;
	private List monthList;
	private List dayList;
	private String empCode;
	private String deviceId;

	private String strStartYear;
	private String strStartMonth;
	private String strStartDay;
	private String strStartYearId;
	private String strStartMonthId;
	private String strStartDayId;

	private String strEndYear;
	private String strEndMonth;
	private String strEndDay;
	private String strEndYearId;
	private String strEndMonthId;
	private String strEndDayId;
	
	private String strStartDate;
	private String strEndDate;
	private String queryType;
	

	/*
	 * ��ҳ
	 */
	 private	String pageNo ;// �ڼ�ҳ
	 private	String pageSize ;// ÿҳ����
	 private	String paramName ;// �����ֶ�����
	 private	String paramValue;// �����ֶ�ֵ
	 private	String vague ;// �����ֶ��Ƿ�ģ��ƥ��

	
	
	private String isImportExcel;
	
	public String getIsImportExcel() {
		return isImportExcel;
	}
	public void setIsImportExcel(String isImportExcel) {
		this.isImportExcel = isImportExcel;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
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
	public void setMonthList(List monthList) {
		this.monthList = monthList;
	}
	public List getDayList() {
		return dayList;
	}
	public void setDayList(List dayList) {
		this.dayList = dayList;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getStrStartYear() {
		return strStartYear;
	}
	public void setStrStartYear(String strStartYear) {
		this.strStartYear = strStartYear;
	}
	public String getStrStartMonth() {
		return strStartMonth;
	}
	public void setStrStartMonth(String strStartMonth) {
		this.strStartMonth = strStartMonth;
	}
	public String getStrStartDay() {
		return strStartDay;
	}
	public void setStrStartDay(String strStartDay) {
		this.strStartDay = strStartDay;
	}
	public String getStrStartYearId() {
		return strStartYearId;
	}
	public void setStrStartYearId(String strStartYearId) {
		this.strStartYearId = strStartYearId;
	}
	public String getStrStartMonthId() {
		return strStartMonthId;
	}
	public void setStrStartMonthId(String strStartMonthId) {
		this.strStartMonthId = strStartMonthId;
	}
	public String getStrStartDayId() {
		return strStartDayId;
	}
	public void setStrStartDayId(String strStartDayId) {
		this.strStartDayId = strStartDayId;
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
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getVague() {
		return vague;
	}
	public void setVague(String vague) {
		this.vague = vague;
	}
	
}

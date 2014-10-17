/**
 * 
 */
package com.sosgps.wzt.stat.funstat.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sosgps.wzt.orm.TAttendanceRule;

/**
 * @author xiaojun.luan
 *
 */
public class AttendanceBean {
	private String mobileNumber;
	private String userName;
	private String attendanceDate;
	private String expectedAreaDesc;
	private float attendanceRate;
	private int attendanceDayNumber;
	private String showAttendanceRate;
	private List ruleList=null;
	
	private ArrayList<AttendancePoint> attendancePointList=new ArrayList<AttendancePoint>();

	private HashMap<String,AttendancePoint> timePointHash=new HashMap<String,AttendancePoint>();
	
	public void addAttendancePoint(String ruleID,String time,String loc_desc,String ruleArea,String result){
		
		AttendancePoint attendancePoint=timePointHash.get(ruleID);
		attendancePoint.setLoc_desc(loc_desc);
		attendancePoint.setTime(time);
		attendancePoint.setRuleArea(ruleArea);
		attendancePoint.setResult(result);
		//attendancePointList.add(attendancePoint);
	}
	private void fillAttendancePoint(TAttendanceRule tAttendanceRule){
		String ruleID=tAttendanceRule.getId()+"";
		String ruleContent=tAttendanceRule.getRuleContent();
		String effectPeriod=tAttendanceRule.getEffectPeriod();

		AttendancePoint attendancePoint=new AttendancePoint();
		attendancePoint.setRuleContent(ruleContent);
		attendancePointList.add(attendancePoint);
		timePointHash.put(ruleID, attendancePoint);
	}
	public void addRuleList(List list){
		for(int i=0;i<list.size();i++){
			TAttendanceRule tAttendanceRule=(TAttendanceRule)list.get(i);
			fillAttendancePoint(tAttendanceRule);
		}
	}
	
	public float getPerDayAttendanceRate(){
		float rate=0l;
		int attendanceTimes=0;
		for(int i=0;i<attendancePointList.size();i++){
			AttendancePoint point=attendancePointList.get(i);
			if(point.getResult()!=null && point.getResult().equals(AttendancePoint.SUCCEED)){
				attendanceTimes++;
			}
		}
		rate=(float)attendanceTimes/attendancePointList.size();
		return rate;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}


	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}





	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getAttendanceDate() {
		return attendanceDate;
	}


	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}


	public String getExpectedAreaDesc() {
		return expectedAreaDesc;
	}


	public void setExpectedAreaDesc(String expectedAreaDesc) {
		this.expectedAreaDesc = expectedAreaDesc;
	}


	public List<AttendancePoint> getAttendancePointList() {
		return attendancePointList;
	}


	public void setAttendancePointList(ArrayList<AttendancePoint> attendancePointList) {
		this.attendancePointList = attendancePointList;
	}
	public List getRuleList() {
		return ruleList;
	}
	public void setRuleList(List ruleList) {
		this.ruleList = ruleList;
	}
	public float getAttendanceRate() {
		return attendanceRate;
	}
	public void setAttendanceRate(float attendanceRate) {
		this.attendanceRate = attendanceRate;
	}
	public int getAttendanceDayNumber() {
		return attendanceDayNumber;
	}
	public void setAttendanceDayNumber(int attendanceDayNumber) {
		this.attendanceDayNumber = attendanceDayNumber;
	}
	public String getShowAttendanceRate() {
		//return showAttendanceRate;
		String pattern="#.##";
		DecimalFormat format=new DecimalFormat(pattern);
		String attendanceRate=format.format(this.getAttendanceRate());
		//System.out.println("attendanceRate:"+attendanceRate);
		double d=0;
		try{
			d=Double.parseDouble(attendanceRate);
		}catch(Exception e){}
		
		return NumberFormat.getPercentInstance().format(d);
	}
	public void setShowAttendanceRate(String showAttendanceRate) {
		this.showAttendanceRate = showAttendanceRate;
	}

	public static void main(String[] s){
		AttendanceBean AttendanceBean=new AttendanceBean();
		AttendanceBean.setAttendanceRate(0.333f);
		System.out.println(AttendanceBean.getShowAttendanceRate());
	}


}

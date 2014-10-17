package com.sosgps.wzt.orm;

import java.util.Date;

/**
 * @author WangChao
 * @description 签到签退实体类
 */
public abstract class AbstractTAttendance implements java.io.Serializable {

	// 主键ID
	private Long id;

	// 终端ID
	//private String deviceId;
	private TTerminal tTerminal;
	
	// 创建时间
	private Date createTime;

	// 考勤日期
	private Integer attendanceDate;

	// 删除标识
	private Integer deleteFlag;

	// 签到时间
	private Date signInTime;

	// 签到经度
	private Float signInLongitude;

	// 签到纬度
	private Float signInLatitude;

	// 签到位置描述
	private String signInDesc;

	// 签退时间
	private Date signOffTime;

	// 签退经度
	private Float signOffLongitude;

	// 签退纬度
	private Float signOffLatitude;

	// 签退位置描述
	private String signOffDesc;
	
	/*
	 * constructor
	 */
	public AbstractTAttendance(){
		
	}
	
	public AbstractTAttendance(TTerminal tTerminal,Date createTime,Integer attendanceDate,Integer deleteFlag,
		Date signInTime, Float signInLongitude, Float signInLatitude, String signInDesc, Date signOffTime, 
		Float signOffLongitude, Float signOffLatitude , String signOffDesc){
		this.tTerminal = tTerminal;
		this.createTime = createTime;
		this.attendanceDate = attendanceDate;
		this.deleteFlag = deleteFlag;
		this.signInTime = signInTime;
		this.signInLongitude = signInLongitude;
		this.signInLatitude = signInLatitude;
		this.signInDesc = signInDesc;
		this.signOffTime = signOffTime;
		this.signOffLongitude = signOffLongitude;
		this.signOffLatitude = signOffLatitude;
		this.signOffDesc = signOffDesc;
		
	}

	//--------getters and setters--------------
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

//	public String getDeviceId() {
//		return deviceId;
//	}
//
//	public void setDeviceId(String deviceId) {
//		this.deviceId = deviceId;
//	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Integer attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}

	public Float getSignInLongitude() {
		return signInLongitude;
	}

	public void setSignInLongitude(Float signInLongitude) {
		this.signInLongitude = signInLongitude;
	}

	public Float getSignInLatitude() {
		return signInLatitude;
	}

	public void setSignInLatitude(Float signInLatitude) {
		this.signInLatitude = signInLatitude;
	}

	public String getSignInDesc() {
		return signInDesc;
	}

	public void setSignInDesc(String signInDesc) {
		this.signInDesc = signInDesc;
	}

	public Date getSignOffTime() {
		return signOffTime;
	}

	public void setSignOffTime(Date signOffTime) {
		this.signOffTime = signOffTime;
	}

	public Float getSignOffLongitude() {
		return signOffLongitude;
	}

	public void setSignOffLongitude(Float signOffLongitude) {
		this.signOffLongitude = signOffLongitude;
	}

	public Float getSignOffLatitude() {
		return signOffLatitude;
	}

	public void setSignOffLatitude(Float signOffLatitude) {
		this.signOffLatitude = signOffLatitude;
	}

	public String getSignOffDesc() {
		return signOffDesc;
	}

	public void setSignOffDesc(String signOffDesc) {
		this.signOffDesc = signOffDesc;
	}


	public TTerminal getTTerminal() {
		return tTerminal;
	}


	public void setTTerminal(TTerminal terminal) {
		tTerminal = terminal;
	}
	

}

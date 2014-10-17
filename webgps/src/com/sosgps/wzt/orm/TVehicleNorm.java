package com.sosgps.wzt.orm;

import java.util.Date;

//@SuppressWarnings("serial")
public class TVehicleNorm implements java.io.Serializable {


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMileageNorm() {
		return mileageNorm;
	}

	public void setMileageNorm(Long mileageNorm) {
		this.mileageNorm = mileageNorm;
	}

	public Long getExpenseNorm() {
		return expenseNorm;
	}

	public void setExpenseNorm(Long expenseNorm) {
		this.expenseNorm = expenseNorm;
	}

	public Long getReturnNorm() {
		return returnNorm;
	}

	public void setReturnNorm(Long returnNorm) {
		this.returnNorm = returnNorm;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deviceId;
	private Long userId;
	private Long mileageNorm;
	private Long expenseNorm;
	private Long returnNorm;
	private Date saveDate;
	private String createMan;
	private String demo;

	public TVehicleNorm(Long userId, String deviceId,Long mileageNorm, Long  expenseNorm,
			Long  returnNorm,Date saveDate,
			String createMan, String demo) {
		this.userId=userId;
		this.deviceId=deviceId;
		this.mileageNorm=mileageNorm;
		this.expenseNorm=expenseNorm;
		this.returnNorm=returnNorm;
		this.saveDate=saveDate;
		this.createMan=createMan;
		this.demo=demo;
	}

	public TVehicleNorm() {
		// TODO Auto-generated constructor stub
	}

}

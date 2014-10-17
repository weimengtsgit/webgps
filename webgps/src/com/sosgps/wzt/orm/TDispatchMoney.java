package com.sosgps.wzt.orm;

import java.util.Date;

//@SuppressWarnings("serial")
public class TDispatchMoney implements java.io.Serializable {

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

	public Long getDispatchAmount() {
		return dispatchAmount;
	}

	public void setDispatchAmount(Long dispatchAmount) {
		this.dispatchAmount = dispatchAmount;
	}

	public Date getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(Date dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	public String getFrmhandler() {
		return frmhandler;
	}

	public void setFrmhandler(String frmhandler) {
		this.frmhandler = frmhandler;
	}

	public String getFrmdemo() {
		return frmdemo;
	}

	public void setFrmdemo(String frmdemo) {
		this.frmdemo = frmdemo;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deviceId;
	private Long userId;
	private Long dispatchAmount;
	private Date  dispatchDate;
	private Date saveDate;
	private String frmhandler;
	private String frmdemo;

	public TDispatchMoney(Long userId, String deviceId,Long dispatchAmount, Date dispatchDate, Date saveDate,
			String frmhandler, String frmdemo) {
		this.userId=userId;
		this.deviceId=deviceId;
		this.dispatchAmount=dispatchAmount;
		this.dispatchDate=dispatchDate;
		this.saveDate=saveDate;
		this.frmhandler=frmhandler;
		this.frmdemo=frmdemo;
	}

	public TDispatchMoney() {
		// TODO Auto-generated constructor stub
	}

	

}

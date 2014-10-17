package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public abstract class AbstractTDiaryMarkTj implements java.io.Serializable {

	private Long id;
	private Date tjDate;
	private String deviceId;
	private String entCode;
	private Long userId;
	private Date diaryDate;
	private Double arrivalRate;

	public Date getTjDate() {
		return tjDate;
	}

	public void setTjDate(Date tjDate) {
		this.tjDate = tjDate;
	}

	/** default constructor */
	public AbstractTDiaryMarkTj() {
	}

	/** full constructor */
	public AbstractTDiaryMarkTj(Long id, Date tjDate, String deviceId, String entCode, Long userId, 
			Date diaryDate, Double arrivalRate) {
		this.id = id;
		this.deviceId = deviceId;
		this.userId = userId;
		this.entCode = entCode;
		this.diaryDate = diaryDate;
		this.tjDate = tjDate;
		this.arrivalRate = arrivalRate;
	}

	public Date getDiaryDate() {
		return diaryDate;
	}

	public void setDiaryDate(Date diaryDate) {
		this.diaryDate = diaryDate;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double getArrivalRate() {
		return arrivalRate;
	}

	public void setArrivalRate(Double arrivalRate) {
		this.arrivalRate = arrivalRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
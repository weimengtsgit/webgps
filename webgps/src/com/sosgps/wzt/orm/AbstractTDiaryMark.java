package com.sosgps.wzt.orm;

import java.util.Date;

@SuppressWarnings("serial")
public abstract class AbstractTDiaryMark implements java.io.Serializable {

	private Long id;
	private Long diaryId;
	private String deviceId;
	private Double longitude;
	private Double latitude;
	private Date createDate;
	private Date changeDate;
	private Long userId;
	private String entCode;
	private Date diaryDate;
	private Long isArrive;
	private Date tjDate;
	private Long distance;

	/** default constructor */
	public AbstractTDiaryMark() {
	}

	/** full constructor */
	public AbstractTDiaryMark(Long id, Long diaryId, String deviceId, Double longitude, Double latitude,
			Date createDate, Date changeDate, Long userId, String entCode, Date diaryDate, Long isArrive,
			Date tjDate, Long distance) {
		this.id = id;
		this.diaryId = diaryId;
		this.deviceId = deviceId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.createDate = createDate;
		this.changeDate = changeDate;
		this.userId = userId;
		this.entCode = entCode;
		this.diaryDate = diaryDate;
		this.isArrive = isArrive;
		this.tjDate = tjDate;
		this.distance = distance;
	}

	public Long getIsArrive() {
		return isArrive;
	}

	public void setIsArrive(Long isArrive) {
		this.isArrive = isArrive;
	}

	public Date getTjDate() {
		return tjDate;
	}

	public void setTjDate(Date tjDate) {
		this.tjDate = tjDate;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getDiaryId() {
		return diaryId;
	}

	public void setDiaryId(Long diaryId) {
		this.diaryId = diaryId;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Date getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
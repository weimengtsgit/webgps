package com.sosgps.wzt.system.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title: TEntValue.java
 * @Description:
 * @Copyright:
 * @Date: 2009-4-7 ÏÂÎç08:00:33
 * @Copyright (c) 2008 Company: www.sosgps.com
 * @author yang.lei
 * @version 1.0
 */
public class TEntValue implements Serializable {
	public TEntValue() {

	}

	private Long id;

	private String entCode;

	private String entName;

	private Date openTime;

	private Date endTime;

	private String entStatus;

	private String centerX;

	private String centerY;

	private Integer mapZoom;

	private String logoUrl;

	private Date entCreateDate;

	private String feeType;

	private String businessId;

	private String areaCode;

	private String entType;

	private String otherInfo;

	private String usageFlag;

	private String maxUserNum;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the entCode
	 */
	public String getEntCode() {
		return entCode;
	}

	/**
	 * @param entCode
	 *            the entCode to set
	 */
	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	/**
	 * @return the entName
	 */
	public String getEntName() {
		return entName;
	}

	/**
	 * @param entName
	 *            the entName to set
	 */
	public void setEntName(String entName) {
		this.entName = entName;
	}

	/**
	 * @return the openTime
	 */
	public Date getOpenTime() {
		return openTime;
	}

	/**
	 * @param openTime
	 *            the openTime to set
	 */
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the entStatus
	 */
	public String getEntStatus() {
		return entStatus;
	}

	/**
	 * @param entStatus
	 *            the entStatus to set
	 */
	public void setEntStatus(String entStatus) {
		this.entStatus = entStatus;
	}

	/**
	 * @return the centerX
	 */
	public String getCenterX() {
		return centerX;
	}

	/**
	 * @param centerX
	 *            the centerX to set
	 */
	public void setCenterX(String centerX) {
		this.centerX = centerX;
	}

	/**
	 * @return the centerY
	 */
	public String getCenterY() {
		return centerY;
	}

	/**
	 * @param centerY
	 *            the centerY to set
	 */
	public void setCenterY(String centerY) {
		this.centerY = centerY;
	}

	/**
	 * @return the mapZoom
	 */
	public Integer getMapZoom() {
		return mapZoom;
	}

	/**
	 * @param mapZoom
	 *            the mapZoom to set
	 */
	public void setMapZoom(Integer mapZoom) {
		this.mapZoom = mapZoom;
	}

	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * @param logoUrl
	 *            the logoUrl to set
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	/**
	 * @return the entCreateDate
	 */
	public Date getEntCreateDate() {
		return entCreateDate;
	}

	/**
	 * @param entCreateDate
	 *            the entCreateDate to set
	 */
	public void setEntCreateDate(Date entCreateDate) {
		this.entCreateDate = entCreateDate;
	}

	/**
	 * @return the feeType
	 */
	public String getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType
	 *            the feeType to set
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	/**
	 * @return the businessId
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * @param businessId
	 *            the businessId to set
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode
	 *            the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the entType
	 */
	public String getEntType() {
		return entType;
	}

	/**
	 * @param entType
	 *            the entType to set
	 */
	public void setEntType(String entType) {
		this.entType = entType;
	}

	/**
	 * @return the otherInfo
	 */
	public String getOtherInfo() {
		return otherInfo;
	}

	/**
	 * @param otherInfo
	 *            the otherInfo to set
	 */
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	/**
	 * @return the usageFlag
	 */
	public String getUsageFlag() {
		return usageFlag;
	}

	/**
	 * @param usageFlag
	 *            the usageFlag to set
	 */
	public void setUsageFlag(String usageFlag) {
		this.usageFlag = usageFlag;
	}

	/**
	 * @return the maxUserNum
	 */
	public String getMaxUserNum() {
		return maxUserNum;
	}

	/**
	 * @param maxUserNum
	 *            the maxUserNum to set
	 */
	public void setMaxUserNum(String maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String toString() {
		final String TAB = ",";
		String retValue = "com.sosgps.wzt.system.model.TEntValue=:{" + "id=" + this.id + TAB + "entCode="
				+ this.entCode + TAB + "entName=" + this.entName + TAB + "openTime=" + this.openTime + TAB
				+ "endTime=" + this.endTime + TAB + "entStatus=" + this.entStatus + TAB + "centerX="
				+ this.centerX + TAB + "centerY=" + this.centerY + TAB + "mapZoom=" + this.mapZoom + TAB
				+ "logoUrl=" + this.logoUrl + TAB + "entCreateDate=" + this.entCreateDate + TAB + "feeType="
				+ this.feeType + TAB + "businessId=" + this.businessId + TAB + "areaCode=" + this.areaCode
				+ TAB + "entType=" + this.entType + TAB + "otherInfo=" + this.otherInfo + TAB + "usageFlag="
				+ this.usageFlag + TAB + "maxUserNum=" + this.maxUserNum + "}";
		return retValue;
	}

}

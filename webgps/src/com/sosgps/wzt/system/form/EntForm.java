/**
 * 
 */
package com.sosgps.wzt.system.form;

import java.util.Date;

/**
 * @author xiaojun.luan
 *
 */
public class EntForm extends HtmlBaseForm {

	private String entCode;
	private String entName;


	private String centerX;
	private String centerY;

	private Integer mapZoom;
	private String logoUrl;

	private String businessId;
	private String areaCode;
	private String entType;

	private String otherInfo;

	private String maxUserNum;

	private String adminUser;
	private String password;
	
	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}

	public String getCenterX() {
		return centerX;
	}

	public void setCenterX(String centerX) {
		this.centerX = centerX;
	}

	public String getCenterY() {
		return centerY;
	}

	public void setCenterY(String centerY) {
		this.centerY = centerY;
	}

	public Integer getMapZoom() {
		return mapZoom;
	}

	public void setMapZoom(Integer mapZoom) {
		this.mapZoom = mapZoom;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getEntType() {
		return entType;
	}

	public void setEntType(String entType) {
		this.entType = entType;
	}

	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}

	public String getMaxUserNum() {
		return maxUserNum;
	}

	public void setMaxUserNum(String maxUserNum) {
		this.maxUserNum = maxUserNum;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}

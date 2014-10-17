package com.sosgps.wzt.orm;

// Generated 2010-4-10 16:44:48 by Hibernate Tools 3.2.5.Beta

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TPoi generated by hbm2java
 */
public class TPoi implements java.io.Serializable {

	private Long id;
	private String poiName;
	private String poiDesc;
	private Long poiType;
	private String creator;
	private Date cdate;
	private String poiDatas;
	private String telephone;
	private String address;
	private String entcode;
	private String keyword;
	private String poiEncryptDatas;
	private String iconpath;
	private String borderLineWidth;
	private String borderLineColor;
	private String borderLineAlpha;
	private String fillColor;
	private String fillAlpha;
	private String visible;
	private Long visitDistance;
	private String locDesc;
	private String deviceId;
	private Long states;
	private Long createOn;
	private Long lastUpdateOn;
	private Set refLayerPois = new HashSet(0);
	private Set refTermPois = new HashSet(0);
	private Set tTerminal = new HashSet(0);

	public TPoi() {
	}

	public TPoi(String poiName, String poiDesc, Long poiType, String creator,
			Date cdate, String poiDatas, String telephone, String address,
			String entcode, String keyword, String poiEncryptDatas,
			String iconpath, String borderLineWidth, String borderLineColor,
			String borderLineAlpha, String fillColor, String fillAlpha,
			String visible, String deviceId, Set refLayerPois, Set refTermPois, 
			Set tTerminal, Long states, Long createOn, Long lastUpdateOn) {
		this.poiName = poiName;
		this.poiDesc = poiDesc;
		this.poiType = poiType;
		this.creator = creator;
		this.cdate = cdate;
		this.poiDatas = poiDatas;
		this.telephone = telephone;
		this.address = address;
		this.entcode = entcode;
		this.keyword = keyword;
		this.poiEncryptDatas = poiEncryptDatas;
		this.iconpath = iconpath;
		this.borderLineWidth = borderLineWidth;
		this.borderLineColor = borderLineColor;
		this.borderLineAlpha = borderLineAlpha;
		this.fillColor = fillColor;
		this.fillAlpha = fillAlpha;
		this.visible = visible;
		this.deviceId = deviceId;
		this.refLayerPois = refLayerPois;
		this.refTermPois = refTermPois;
		this.tTerminal = tTerminal;
		this.states = states;
		this.createOn = createOn;
		this.lastUpdateOn = lastUpdateOn;
	}

	public Long getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Long createOn) {
		this.createOn = createOn;
	}

	public Long getLastUpdateOn() {
		return lastUpdateOn;
	}

	public void setLastUpdateOn(Long lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}

	public Long getStates() {
		return states;
	}

	public void setStates(Long states) {
		this.states = states;
	}

	public Set gettTerminal() {
		return tTerminal;
	}

	public void settTerminal(Set tTerminal) {
		this.tTerminal = tTerminal;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPoiName() {
		return this.poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getPoiDesc() {
		return this.poiDesc;
	}

	public void setPoiDesc(String poiDesc) {
		this.poiDesc = poiDesc;
	}

	public Long getPoiType() {
		return this.poiType;
	}

	public void setPoiType(Long poiType) {
		this.poiType = poiType;
	}

	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCdate() {
		return this.cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public String getPoiDatas() {
		return this.poiDatas;
	}

	public void setPoiDatas(String poiDatas) {
		this.poiDatas = poiDatas;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEntcode() {
		return this.entcode;
	}

	public void setEntcode(String entcode) {
		this.entcode = entcode;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getPoiEncryptDatas() {
		return this.poiEncryptDatas;
	}

	public void setPoiEncryptDatas(String poiEncryptDatas) {
		this.poiEncryptDatas = poiEncryptDatas;
	}

	public String getIconpath() {
		return this.iconpath;
	}

	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}

	public String getBorderLineWidth() {
		return this.borderLineWidth;
	}

	public void setBorderLineWidth(String borderLineWidth) {
		this.borderLineWidth = borderLineWidth;
	}

	public String getBorderLineColor() {
		return this.borderLineColor;
	}

	public void setBorderLineColor(String borderLineColor) {
		this.borderLineColor = borderLineColor;
	}

	public String getBorderLineAlpha() {
		return this.borderLineAlpha;
	}

	public void setBorderLineAlpha(String borderLineAlpha) {
		this.borderLineAlpha = borderLineAlpha;
	}

	public String getFillColor() {
		return this.fillColor;
	}

	public void setFillColor(String fillColor) {
		this.fillColor = fillColor;
	}

	public String getFillAlpha() {
		return this.fillAlpha;
	}

	public void setFillAlpha(String fillAlpha) {
		this.fillAlpha = fillAlpha;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public Set getRefLayerPois() {
		return this.refLayerPois;
	}

	public void setRefLayerPois(Set refLayerPois) {
		this.refLayerPois = refLayerPois;
	}

	public Set getRefTermPois() {
		return this.refTermPois;
	}

	public void setRefTermPois(Set refTermPois) {
		this.refTermPois = refTermPois;
	}

	public Long getVisitDistance() {
		return visitDistance;
	}

	public void setVisitDistance(Long visitDistance) {
		this.visitDistance = visitDistance;
	}

	public String getLocDesc() {
		return locDesc;
	}

	public void setLocDesc(String locDesc) {
		this.locDesc = locDesc;
	}

}

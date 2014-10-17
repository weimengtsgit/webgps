package com.sosgps.v21.model;

public class Promotion implements java.io.Serializable {

	private long id;
	private String deviceId;
	private Long poiId;
	
	private Double promotion1;
	private Double promotion2;
	private Double promotion3;
	private Double promotion4;
	private Double promotion5;
	private Double promotion6;
	private Double promotion7;
	private Double promotion8;
	private Double promotion9;
	private Double promotion10;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public Double getPromotion1() {
		return promotion1;
	}

	public void setPromotion1(Double promotion1) {
		this.promotion1 = promotion1;
	}

	public Double getPromotion2() {
		return promotion2;
	}

	public void setPromotion2(Double promotion2) {
		this.promotion2 = promotion2;
	}

	public Double getPromotion3() {
		return promotion3;
	}

	public void setPromotion3(Double promotion3) {
		this.promotion3 = promotion3;
	}

	public Double getPromotion4() {
		return promotion4;
	}

	public void setPromotion4(Double promotion4) {
		this.promotion4 = promotion4;
	}

	public Double getPromotion5() {
		return promotion5;
	}

	public void setPromotion5(Double promotion5) {
		this.promotion5 = promotion5;
	}

	public Double getPromotion6() {
		return promotion6;
	}

	public void setPromotion6(Double promotion6) {
		this.promotion6 = promotion6;
	}

	public Double getPromotion7() {
		return promotion7;
	}

	public void setPromotion7(Double promotion7) {
		this.promotion7 = promotion7;
	}

	public Double getPromotion8() {
		return promotion8;
	}

	public void setPromotion8(Double promotion8) {
		this.promotion8 = promotion8;
	}

	public Double getPromotion9() {
		return promotion9;
	}

	public void setPromotion9(Double promotion9) {
		this.promotion9 = promotion9;
	}

	public Double getPromotion10() {
		return promotion10;
	}

	public void setPromotion10(Double promotion10) {
		this.promotion10 = promotion10;
	}
	private String terminalName;
	private Long renderTime;
	private Integer approved = 0;
	private String verifier;
	
	private Integer states = 0;
	private String createBy;
	private String lastUpdateBy;
	private Long createOn;
	private Long lastUpdateOn;
	
	private String poiName;
	private String groupName;
	private Long groupId;
	private String vehicleNumber;
	private String entCode;
	private Long entId;

	public Long getRenderTime() {
		return renderTime;
	}

	public void setRenderTime(Long renderTime) {
		this.renderTime = renderTime;
	}

	public Integer getApproved() {
		return approved;
	}

	public void setApproved(Integer approved) {
		this.approved = approved;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getEntCode() {
		return entCode;
	}

	public void setEntCode(String entCode) {
		this.entCode = entCode;
	}

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}
}

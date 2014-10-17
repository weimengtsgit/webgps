package com.sosgps.v21.model;

import java.math.BigDecimal;

public class SignBill implements java.io.Serializable {

    private static final long serialVersionUID = -4064647033451834689L;

    private Long id;

    private String deviceId;

    private Long poiId;

    private BigDecimal signBillAmount;

    private Long renderTime;

    /** δ��� */
    private Integer approved = 0;

    private String verifier;

    /** δɾ�� */
    private Integer states = 0;

    private String createBy;

    private String lastUpdateBy;

    private Long createOn;

    private Long lastUpdateOn;

    private String groupName;

    private String vehicleNumber;

    private String poiName;

    private String entCode;

    private Long entId;

    private Long groupId;
    
	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	private String terminalName;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

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

    public Long getPoiId() {
        return poiId;
    }

    public void setPoiId(Long poiId) {
        this.poiId = poiId;
    }

    public BigDecimal getSignBillAmount() {
        return signBillAmount;
    }

    public void setSignBillAmount(BigDecimal signBillAmount) {
        this.signBillAmount = signBillAmount;
    }

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

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
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

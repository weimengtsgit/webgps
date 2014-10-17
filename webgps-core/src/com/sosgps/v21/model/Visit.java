package com.sosgps.v21.model;

/**
 * �ͻ��ݷ�
 * 
 * @author wangchao
 * 
 */
public class Visit implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String deviceId;

	private Long poiId;

	private Double signInLng; //签到经度

	private Double signInLat;// 签到纬度

	private Double signInDistance;//签到偏差（米）

	private String signInDesc;  //签到位置的描述

	private Long signInTime; //签到入库时间

	private Long signInRenderTime;//签到手机上报时间

	private Double signOutLng;

	private Double signOutLat;

	private Double signOutDistance;

	private String signOutDesc;

	private Long signOutTime;

	private Long signOutRenderTime;

	private Integer states = 0;

	private Integer validateFlag = 0;

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
	
	//终端名称 add by wangzhen
	private String termName;
	
	
	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public Long getLocationTypeIn() {
		return locationTypeIn;
	}

	public void setLocationTypeIn(Long locationTypeIn) {
		this.locationTypeIn = locationTypeIn;
	}

	public Long getLocationTypeOut() {
		return locationTypeOut;
	}

	public void setLocationTypeOut(Long locationTypeOut) {
		this.locationTypeOut = locationTypeOut;
	}

	private Long locationTypeIn;
	
	private Long locationTypeOut;


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

	public Double getSignInLng() {
		return signInLng;
	}

	public void setSignInLng(Double signInLng) {
		this.signInLng = signInLng;
	}

	public Double getSignInLat() {
		return signInLat;
	}

	public void setSignInLat(Double signInLat) {
		this.signInLat = signInLat;
	}

	public Double getSignInDistance() {
		return signInDistance;
	}

	public void setSignInDistance(Double signInDistance) {
		this.signInDistance = signInDistance;
	}

	public String getSignInDesc() {
		return signInDesc;
	}

	public void setSignInDesc(String signInDesc) {
		this.signInDesc = signInDesc;
	}

	public Long getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Long signInTime) {
		this.signInTime = signInTime;
	}

	public Long getSignInRenderTime() {
		return signInRenderTime;
	}

	public void setSignInRenderTime(Long signInRenderTime) {
		this.signInRenderTime = signInRenderTime;
	}

	public Double getSignOutLng() {
		return signOutLng;
	}

	public void setSignOutLng(Double signOutLng) {
		this.signOutLng = signOutLng;
	}

	public Double getSignOutLat() {
		return signOutLat;
	}

	public void setSignOutLat(Double signOutLat) {
		this.signOutLat = signOutLat;
	}

	public Double getSignOutDistance() {
		return signOutDistance;
	}

	public void setSignOutDistance(Double signOutDistance) {
		this.signOutDistance = signOutDistance;
	}

	public String getSignOutDesc() {
		return signOutDesc;
	}

	public void setSignOutDesc(String signOutDesc) {
		this.signOutDesc = signOutDesc;
	}

	public Long getSignOutTime() {
		return signOutTime;
	}

	public void setSignOutTime(Long signOutTime) {
		this.signOutTime = signOutTime;
	}

	public Long getSignOutRenderTime() {
		return signOutRenderTime;
	}

	public void setSignOutRenderTime(Long signOutRenderTime) {
		this.signOutRenderTime = signOutRenderTime;
	}

	public Integer getStates() {
		return states;
	}

	public void setStates(Integer states) {
		this.states = states;
	}

	public Integer getValidateFlag() {
		return validateFlag;
	}

	public void setValidateFlag(Integer validateFlag) {
		this.validateFlag = validateFlag;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}

package com.sosgps.v21.model;


import java.util.Date;

/**
 * 差旅信息汇总
 * 
 */
public class TravelCost implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deviceId;
	private String entCode;
	
    private Long entId;
	private String termName;//员工姓名
	private String simcard;//手机号
	private String groupName;//部门
	private Long groupId;
	
    private Integer flag;//出差是否完毕：0，本次出差未完毕；1，本次出差完毕
    private Integer reviewStates;//审核状态：0,代表未审核，1代表审核
	//开始出差
	private String leavePlace;//出发地
	private String arrivePlace;//目的地
	private String task;//任务
	private Date startTravelRenderTime;//开始出差手机上报时间
	private Date startTravelTime;// 开始出差时间(服务器入库时间）
    private Float startTravelLongitude;// 开始出差经度(手机上报经度）
    private Float startTravelLatitude;// 开始出差纬度
    private String startTravelDesc;// 开始出差位置描述
    private Integer startLocationType;//开始出差上传的定位类型：0：GPS，1：基站
    
    //结束出差
	private Integer traffic;//承运交通工具
	private Double trafficCost;//承运交通工具费用
	private Integer trafficBills;//承运交通工具账单
	private Integer hotelBills;//住宿账单
	private Double hotelCost;//住宿费用
	private Double subsidyDay;//补助天数
	private Integer subsidyBills;//补助账单
	private Double subsidyCost;//补助费用
	private Integer cityTrafficBills;//市内交通账单
    private Double cityTrafficCost;//市内交通费用
	private String otherIterms;//其他项目
	private Integer otherBills;//其他账单
	private Double otherCost;//其他补助费用
	private Date endTravelRenderTime;//结束出差手机上报时间
    private Date endTravelTime;// 结束出差时间
    private Float endTravelLongitude;// 结束出差经度
    private Float endTravelLatitude;// 结束出差纬度
    private String endTravelDesc;// 结束出差位置描述
    private Integer endLocationType;//结束出差上传的定位类型：0：GPS，1：基站
    //公共部分
	private Date createOn;
	private Date lastUpdateOn;
	private String createBy;
	private String lastUpdateBy;
	private Long deleteFlag;
    
    public TravelCost() {
    }
    
    public TravelCost(Long id, String deviceId, String entCode, Long entId, String termName,
            String simcard, String groupName, Long groupId, Integer flag, Integer reviewStates,
            String leavePlace, String arrivePlace, String task, Date startTravelRenderTime,
            Date startTravelTime, Float startTravelLongitude, Float startTravelLatitude,
            String startTravelDesc, Integer startLocationType, Integer traffic, Double trafficCost,
            Integer trafficBills, Integer hotelBills, Double hotelCost, Double subsidyDay,
            Integer subsidyBills, Double subsidyCost, Integer cityTrafficBills,
            Double cityTrafficCost, String otherIterms, Integer otherBills, Double otherCost,
            Date endTravelRenderTime, Date endTravelTime, Float endTravelLongitude,
            Float endTravelLatitude, String endTravelDesc, Integer endLocationType, Date createOn,
            Date lastUpdateOn, String createBy, String lastUpdateBy, Long deleteFlag) {
        this.id = id;
        this.deviceId = deviceId;
        this.entCode = entCode;
        this.entId = entId;
        this.termName = termName;
        this.simcard = simcard;
        this.groupName = groupName;
        this.groupId = groupId;
        this.flag = flag;
        this.reviewStates = reviewStates;
        this.leavePlace = leavePlace;
        this.arrivePlace = arrivePlace;
        this.task = task;
        this.startTravelRenderTime = startTravelRenderTime;
        this.startTravelTime = startTravelTime;
        this.startTravelLongitude = startTravelLongitude;
        this.startTravelLatitude = startTravelLatitude;
        this.startTravelDesc = startTravelDesc;
        this.startLocationType = startLocationType;
        this.traffic = traffic;
        this.trafficCost = trafficCost;
        this.trafficBills = trafficBills;
        this.hotelBills = hotelBills;
        this.hotelCost = hotelCost;
        this.subsidyDay = subsidyDay;
        this.subsidyBills = subsidyBills;
        this.subsidyCost = subsidyCost;
        this.cityTrafficBills = cityTrafficBills;
        this.cityTrafficCost = cityTrafficCost;
        this.otherIterms = otherIterms;
        this.otherBills = otherBills;
        this.otherCost = otherCost;
        this.endTravelRenderTime = endTravelRenderTime;
        this.endTravelTime = endTravelTime;
        this.endTravelLongitude = endTravelLongitude;
        this.endTravelLatitude = endTravelLatitude;
        this.endTravelDesc = endTravelDesc;
        this.endLocationType = endLocationType;
        this.createOn = createOn;
        this.lastUpdateOn = lastUpdateOn;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.deleteFlag = deleteFlag;
    }


    public Long getEntId() {
        return entId;
    }
    
    public void setEntId(Long entId) {
        this.entId = entId;
    }
    
    public String getTermName() {
        return termName;
    }
    
    public void setTermName(String termName) {
        this.termName = termName;
    }
    
    public String getSimcard() {
        return simcard;
    }
    
    public void setSimcard(String simcard) {
        this.simcard = simcard;
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
    public Integer getReviewStates() {
        return reviewStates;
    }
    
    public void setReviewStates(Integer reviewStates) {
        this.reviewStates = reviewStates;
    }
    public Integer getStartLocationType() {
        return startLocationType;
    }
    
    public void setStartLocationType(Integer startLocationType) {
        this.startLocationType = startLocationType;
    }
    
    public Integer getEndLocationType() {
        return endLocationType;
    }
    
    public void setEndLocationType(Integer endLocationType) {
        this.endLocationType = endLocationType;
    }
    public Integer getFlag() {
        return flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    public Date getStartTravelTime() {
        return startTravelTime;
    }
    
    public void setStartTravelTime(Date startTravelTime) {
        this.startTravelTime = startTravelTime;
    }
    
    public Float getStartTravelLongitude() {
        return startTravelLongitude;
    }
    
    public void setStartTravelLongitude(Float startTravelLongitude) {
        this.startTravelLongitude = startTravelLongitude;
    }
    
    public Float getStartTravelLatitude() {
        return startTravelLatitude;
    }
    
    public void setStartTravelLatitude(Float startTravelLatitude) {
        this.startTravelLatitude = startTravelLatitude;
    }
    
    public String getStartTravelDesc() {
        return startTravelDesc;
    }
    
    public void setStartTravelDesc(String startTravelDesc) {
        this.startTravelDesc = startTravelDesc;
    }
    public Date getEndTravelTime() {
        return endTravelTime;
    }
    
    public void setEndTravelTime(Date endTravelTime) {
        this.endTravelTime = endTravelTime;
    }
    
    public Float getEndTravelLongitude() {
        return endTravelLongitude;
    }
    
    public void setEndTravelLongitude(Float endTravelLongitude) {
        this.endTravelLongitude = endTravelLongitude;
    }
    
    public Float getEndTravelLatitude() {
        return endTravelLatitude;
    }
    
    public void setEndTravelLatitude(Float endTravelLatitude) {
        this.endTravelLatitude = endTravelLatitude;
    }
    
    public String getEndTravelDesc() {
        return endTravelDesc;
    }
    
    public void setEndTravelDesc(String endTravelDesc) {
        this.endTravelDesc = endTravelDesc;
    }
    public String getEntCode() {
        return entCode;
    }

    
    public void setEntCode(String entCode) {
        this.entCode = entCode;
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
    
    public String getLeavePlace() {
        return leavePlace;
    }
    
    public void setLeavePlace(String leavePlace) {
        this.leavePlace = leavePlace;
    }
    
    public String getArrivePlace() {
        return arrivePlace;
    }
    
    public void setArrivePlace(String arrivePlace) {
        this.arrivePlace = arrivePlace;
    }
    
    public String getTask() {
        return task;
    }
    
    public void setTask(String task) {
        this.task = task;
    }
    
    public Integer getTraffic() {
        return traffic;
    }
    
    public void setTraffic(Integer traffic) {
        this.traffic = traffic;
    }
    
    public Double getTrafficCost() {
        return trafficCost;
    }
    
    public void setTrafficCost(Double trafficCost) {
        this.trafficCost = trafficCost;
    }
    
    public Integer getTrafficBills() {
        return trafficBills;
    }
    
    public void setTrafficBills(Integer trafficBills) {
        this.trafficBills = trafficBills;
    }
    
    public Integer getHotelBills() {
        return hotelBills;
    }
    
    public void setHotelBills(Integer hotelBills) {
        this.hotelBills = hotelBills;
    }
    
    public Double getHotelCost() {
        return hotelCost;
    }
    
    public void setHotelCost(Double hotelCost) {
        this.hotelCost = hotelCost;
    }
    
    public Double getSubsidyDay() {
        return subsidyDay;
    }
    
    public void setSubsidyDay(Double subsidyDay) {
        this.subsidyDay = subsidyDay;
    }
    
    public Integer getSubsidyBills() {
        return subsidyBills;
    }
    
    public void setSubsidyBills(Integer subsidyBills) {
        this.subsidyBills = subsidyBills;
    }
    
    public Double getSubsidyCost() {
        return subsidyCost;
    }
    
    public void setSubsidyCost(Double subsidyCost) {
        this.subsidyCost = subsidyCost;
    }
    
    public Integer getCityTrafficBills() {
        return cityTrafficBills;
    }
    
    public void setCityTrafficBills(Integer cityTrafficBills) {
        this.cityTrafficBills = cityTrafficBills;
    }
    
    public Double getCityTrafficCost() {
        return cityTrafficCost;
    }
    
    public void setCityTrafficCost(Double cityTrafficCost) {
        this.cityTrafficCost = cityTrafficCost;
    }
    
    public String getOtherIterms() {
        return otherIterms;
    }
    
    public void setOtherIterms(String otherIterms) {
        this.otherIterms = otherIterms;
    }
    
    public Integer getOtherBills() {
        return otherBills;
    }
    
    public void setOtherBills(Integer otherBills) {
        this.otherBills = otherBills;
    }
    
    
    public Double getOtherCost() {
        return otherCost;
    }

    
    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public Date getStartTravelRenderTime() {
        return startTravelRenderTime;
    }

    
    public void setStartTravelRenderTime(Date startTravelRenderTime) {
        this.startTravelRenderTime = startTravelRenderTime;
    }

    
    public Date getEndTravelRenderTime() {
        return endTravelRenderTime;
    }

    
    public void setEndTravelRenderTime(Date endTravelRenderTime) {
        this.endTravelRenderTime = endTravelRenderTime;
    }

    public Date getCreateOn() {
        return createOn;
    }
    
    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }
    
    public Date getLastUpdateOn() {
        return lastUpdateOn;
    }
    
    public void setLastUpdateOn(Date lastUpdateOn) {
        this.lastUpdateOn = lastUpdateOn;
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
    
    public Long getDeleteFlag() {
        return deleteFlag;
    }
    
    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}
package com.sosgps.v21.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工考勤信息汇总
 * 
 */
public class EmployeeAttend implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String deviceId;
    private String entCode;
    private Long entId;
    private String termName;//员工姓名
    private String simcard;
    private String groupName;
    private Long groupId;
    
    private Integer attendStates;//员工出勤状态
    private Integer attendanceDate;// 考勤日期
    
    //请假
    private Integer vacateCause;//请假事由
    private Double  vacateDay;//请假天数
    private Date vacateRenderTime;//请假手机上报时间
    private Integer vacateFlag;//请假标示：0 标示半天假
    //签到
    private Date signInTime;// 签到时间(服务器入库时间）
    private Float signInLongitude;// 签到经度(手机上报经度）
    private Float signInLatitude;// 签到纬度
    private String signInDesc;// 签到位置描述
    private Date signInRenderTime;//签到手机上报时间
    private Integer signInLocationType;//签到上传的定位类型：0：GPS，1：基站
    //签退
    private Date signOffTime;// 签退时间
    private Float signOffLongitude;// 签退经度
    private Float signOffLatitude;// 签退纬度
    private String signOffDesc;// 签退位置描述
    private Date signOffRenderTime;//签退手机上报时间
    private Integer signOffLocationType;//签退上传的定位类型：0：GPS，1：基站
    //展示数据
    private Map<Integer,Integer> attendStateMap = new HashMap<Integer,Integer>();
    private Integer noAttendDays;//缺勤天数
    private Integer noWorkDays;//脱岗天数
    private Integer attendDays;//出勤天数
    private Integer lateOutDays;//迟到早退天数
    private Integer vacateDays;//请假天数
    private Long travelCostId;//差旅信息Id
    
    private Date createOn;
    private Date lastUpdateOn;
    private String createBy;
    private String lastUpdateBy;
    private Long deleteFlag;
    
    
    public EmployeeAttend() {
    }
    public EmployeeAttend(Long id, String deviceId, String entCode, Long entId, String termName,
            String simcard, String groupName, Long groupId, Integer attendStates,
            Integer attendanceDate, Integer vacateCause, Double vacateDay, Date vacateRenderTime,
            Date signInTime, Float signInLongitude, Float signInLatitude, String signInDesc,
            Date signInRenderTime, Integer signInLocationType, Date signOffTime,
            Float signOffLongitude, Float signOffLatitude, String signOffDesc,
            Date signOffRenderTime, Integer signOffLocationType, Date createOn, Date lastUpdateOn,
            String createBy, String lastUpdateBy, Long deleteFlag) {
        super();
        this.id = id;
        this.deviceId = deviceId;
        this.entCode = entCode;
        this.entId = entId;
        this.termName = termName;
        this.simcard = simcard;
        this.groupName = groupName;
        this.groupId = groupId;
        this.attendStates = attendStates;
        this.attendanceDate = attendanceDate;
        this.vacateCause = vacateCause;
        this.vacateDay = vacateDay;
        this.vacateRenderTime = vacateRenderTime;
        this.signInTime = signInTime;
        this.signInLongitude = signInLongitude;
        this.signInLatitude = signInLatitude;
        this.signInDesc = signInDesc;
        this.signInRenderTime = signInRenderTime;
        this.signInLocationType = signInLocationType;
        this.signOffTime = signOffTime;
        this.signOffLongitude = signOffLongitude;
        this.signOffLatitude = signOffLatitude;
        this.signOffDesc = signOffDesc;
        this.signOffRenderTime = signOffRenderTime;
        this.signOffLocationType = signOffLocationType;
        this.createOn = createOn;
        this.lastUpdateOn = lastUpdateOn;
        this.createBy = createBy;
        this.lastUpdateBy = lastUpdateBy;
        this.deleteFlag = deleteFlag;
    }
    
    public Integer getVacateFlag() {
        return vacateFlag;
    }
    
    public void setVacateFlag(Integer vacateFlag) {
        this.vacateFlag = vacateFlag;
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
    public Integer getSignInLocationType() {
        return signInLocationType;
    }
    
    public void setSignInLocationType(Integer signInLocationType) {
        this.signInLocationType = signInLocationType;
    }
    
    public Integer getSignOffLocationType() {
        return signOffLocationType;
    }
    
    public void setSignOffLocationType(Integer signOffLocationType) {
        this.signOffLocationType = signOffLocationType;
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
    
    public String getEntCode() {
        return entCode;
    }
    
    public void setEntCode(String entCode) {
        this.entCode = entCode;
    }
    
    public Integer getAttendStates() {
        return attendStates;
    }
    
    public void setAttendStates(Integer attendStates) {
        this.attendStates = attendStates;
    }
    
    public Integer getVacateCause() {
        return vacateCause;
    }
    
    public void setVacateCause(Integer vacateCause) {
        this.vacateCause = vacateCause;
    }
    
    public Double getVacateDay() {
        return vacateDay;
    }
    
    public void setVacateDay(Double vacateDay) {
        this.vacateDay = vacateDay;
    }
    
    public Integer getAttendanceDate() {
        return attendanceDate;
    }
    
    public void setAttendanceDate(Integer attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    
    public Date getSignInTime() {
        return signInTime;
    }
    
    public void setSignInTime(Date signInTime) {
        this.signInTime = signInTime;
    }
    
    public Float getSignInLongitude() {
        return signInLongitude;
    }
    
    public void setSignInLongitude(Float signInLongitude) {
        this.signInLongitude = signInLongitude;
    }
    
    public Float getSignInLatitude() {
        return signInLatitude;
    }
    
    public void setSignInLatitude(Float signInLatitude) {
        this.signInLatitude = signInLatitude;
    }
    
    public String getSignInDesc() {
        return signInDesc;
    }
    
    public void setSignInDesc(String signInDesc) {
        this.signInDesc = signInDesc;
    }
    
    public Date getSignOffTime() {
        return signOffTime;
    }
    
    public void setSignOffTime(Date signOffTime) {
        this.signOffTime = signOffTime;
    }
    
    public Float getSignOffLongitude() {
        return signOffLongitude;
    }
    
    public void setSignOffLongitude(Float signOffLongitude) {
        this.signOffLongitude = signOffLongitude;
    }
    
    public Float getSignOffLatitude() {
        return signOffLatitude;
    }
    
    public void setSignOffLatitude(Float signOffLatitude) {
        this.signOffLatitude = signOffLatitude;
    }
    
    public String getSignOffDesc() {
        return signOffDesc;
    }
    
    public void setSignOffDesc(String signOffDesc) {
        this.signOffDesc = signOffDesc;
    }
    
    public Date getSignInRenderTime() {
        return signInRenderTime;
    }
    
    public void setSignInRenderTime(Date signInRenderTime) {
        this.signInRenderTime = signInRenderTime;
    }
    
    public Date getSignOffRenderTime() {
        return signOffRenderTime;
    }
    
    public void setSignOffRenderTime(Date signOffRenderTime) {
        this.signOffRenderTime = signOffRenderTime;
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
    
    public Date getVacateRenderTime() {
        return vacateRenderTime;
    }
    
    public void setVacateRenderTime(Date vacateRenderTime) {
        this.vacateRenderTime = vacateRenderTime;
    }
    
    public Map<Integer, Integer> getAttendStateMap() {
        return attendStateMap;
    }
    
    public void setAttendStateMap(Map<Integer, Integer> attendStateMap) {
        this.attendStateMap = attendStateMap;
    }
    
    public Integer getNoAttendDays() {
        return noAttendDays;
    }
    
    public void setNoAttendDays(Integer noAttendDays) {
        this.noAttendDays = noAttendDays;
    }
    
    public Integer getNoWorkDays() {
        return noWorkDays;
    }
    
    public void setNoWorkDays(Integer noWorkDays) {
        this.noWorkDays = noWorkDays;
    }
    
    public Integer getAttendDays() {
        return attendDays;
    }
    
    public void setAttendDays(Integer attendDays) {
        this.attendDays = attendDays;
    }
    
    public Integer getLateOutDays() {
        return lateOutDays;
    }
    
    public void setLateOutDays(Integer lateOutDays) {
        this.lateOutDays = lateOutDays;
    }
    
    public Integer getVacateDays() {
        return vacateDays;
    }
    
    public void setVacateDays(Integer vacateDays) {
        this.vacateDays = vacateDays;
    }
    
    public Long getTravelCostId() {
        return travelCostId;
    }
    
    public void setTravelCostId(Long travelCostId) {
        this.travelCostId = travelCostId;
    }
    
}
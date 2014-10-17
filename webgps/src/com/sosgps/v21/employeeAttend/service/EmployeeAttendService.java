package com.sosgps.v21.employeeAttend.service;

import java.util.List;

import com.sosgps.v21.model.EmployeeAttend;

/**
 * 员工考勤表服务类
 * @author Administrator
 *
 */
public interface EmployeeAttendService {
    
    /**
     * 通过年月如：201204查询考勤数据
     * @param entCode
     * @param deviceIds :多个终端
     * @param deviceIds :年月
     * @return
     */
    List<EmployeeAttend> getEmployeeAttendByCondition(String entCode,String deviceIds,String attendDate);

    /**
     * 通过考勤年月日查出该员工的考勤信息
     * @param entCode
     * @param deviceId :单个终端
     * @param deviceIds :年月
     * @return
     */
   public EmployeeAttend getAttendStates(Integer attendStates, String deviceId, String entCode);

   /**
    * 通过当前日期判断这之前的最近的日期有没有请假
    * 若有请假,读取请假时间+请假的时间 >=当前时间 否则没有请假
    * @param deviceId
    * @param entCode
    * @param attendanceDate
    * @param attendStates
    */
   public boolean judgeVacate(String deviceId, String entCode, Integer attendanceDate, int attendStates);
   
   /**
    * 获取公司级别的数据
    * @param entCode
    * @param deviceIds
    * @param attendanceDateI
    * @param utcStartDate
    * @param utcEndDate
    * @return
    */
   public List<Object[]> getCompanyAttendData(String entCode, String deviceIds, Integer attendanceDateI,
        String utcStartDate, String utcEndDate,Integer atteStartDateI,Integer atteEndDateI);
   
   /**
    * 通过Id获取个别员工的考勤
    * @param empAtteId
    * @return
    */
   public EmployeeAttend getEmployeeAttendById(Long empAtteId);
   
   /**
    * 通过企业代码，终端ID，考勤日期，考勤状态获取对象
    * @param entCode
    * @param deviceId
    * @param attendDate
    * @param states
    * @return
    */
   public EmployeeAttend getEmployeeAttendByDeviceIdAndDateAndStates(String entCode, String deviceId,
        Integer attendDate, Integer states);


}

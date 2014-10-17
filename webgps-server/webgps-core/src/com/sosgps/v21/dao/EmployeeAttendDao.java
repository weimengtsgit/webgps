package com.sosgps.v21.dao;

import java.util.List;

import com.sosgps.v21.model.EmployeeAttend;

public interface EmployeeAttendDao {
    public List<EmployeeAttend> findEmployeeAttendByCondition(String entCode,String deviceIds,String startAttendDate,String endAttendDate);
    EmployeeAttend queryEmployeeAttendByCondition(Integer attendStates, String deviceId,
            String entCode);
    public List<EmployeeAttend> findEmployeeAttendByParam(String entCode, String deviceId,
            Integer attendanceDate, int attendStates);
    public List<Object[]> findCompanyAttendData(String entCode, String deviceIds,
            Integer attendanceDateI, String utcStartDate, String utcEndDate,Integer atteStartDateI,Integer atteEndDateI);
    public EmployeeAttend getEmployeeAttendById(Long empAtteId);
    public EmployeeAttend getEmployeeAttendByDeviceIdAndDateAndStates(String entCode, String deviceId,
            Integer attendDate, Integer states);
    
}

package com.sosgps.v21.employeeAttend.service.impl;

import java.util.List;

import com.sosgps.v21.dao.EmployeeAttendDao;
import com.sosgps.v21.employeeAttend.service.EmployeeAttendService;
import com.sosgps.v21.model.EmployeeAttend;


public class EmployeeAttendServiceImpl implements EmployeeAttendService {

    private EmployeeAttendDao employeeAttendDao;
    public EmployeeAttendDao getEmployeeAttendDao() {
        return employeeAttendDao;
    }
    public void setEmployeeAttendDao(EmployeeAttendDao employeeAttendDao) {
        this.employeeAttendDao = employeeAttendDao;
    }
    
    public List<EmployeeAttend> getEmployeeAttendByCondition(String entCode,String deviceIds,String attendDate) {
        //1号到31号
        String startAttendDate = attendDate + "01";
        String endAttendDate = attendDate + "31";
        return employeeAttendDao.findEmployeeAttendByCondition(entCode,deviceIds,startAttendDate,endAttendDate);
    }
    
    public EmployeeAttend getAttendStates(Integer attendStates, String deviceId, String entCode) {
        return employeeAttendDao.queryEmployeeAttendByCondition(attendStates,deviceId,entCode);
    }
    public boolean judgeVacate(String deviceId, String entCode, Integer attendanceDate,
            int attendStates) {
        EmployeeAttend empAtt = null;
        List<EmployeeAttend> empAtts = employeeAttendDao.findEmployeeAttendByParam(entCode, deviceId,attendanceDate,attendStates);
        if(empAtts != null && empAtts.size() > 0) {
            empAtt = empAtts.get(0);
            Integer vacateDate = empAtt.getAttendanceDate() + (int)Math.ceil(empAtt.getVacateDay());
            if(vacateDate >= attendanceDate) {
                //有请假
                return true;
            }
        }
        if(empAtt == null) {
            //没有请假
            return false;
        }
        return false;
    }
    public List<Object[]> getCompanyAttendData(String entCode, String deviceIds,
            Integer attendanceDateI, String utcStartDate, String utcEndDate,Integer atteStartDateI,Integer atteEndDateI) {
        return employeeAttendDao.findCompanyAttendData(entCode,deviceIds,attendanceDateI,utcStartDate,utcEndDate,atteStartDateI,atteEndDateI);
    }
    public EmployeeAttend getEmployeeAttendById(Long empAtteId) {
        return employeeAttendDao.getEmployeeAttendById(empAtteId);
    }
    public EmployeeAttend getEmployeeAttendByDeviceIdAndDateAndStates(String entCode,
            String deviceId, Integer attendDate, Integer states) {
        return employeeAttendDao.getEmployeeAttendByDeviceIdAndDateAndStates(entCode,deviceId,attendDate,states);
    }
   
    
}

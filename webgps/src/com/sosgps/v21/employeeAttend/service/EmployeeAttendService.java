package com.sosgps.v21.employeeAttend.service;

import java.util.List;

import com.sosgps.v21.model.EmployeeAttend;

/**
 * Ա�����ڱ������
 * @author Administrator
 *
 */
public interface EmployeeAttendService {
    
    /**
     * ͨ�������磺201204��ѯ��������
     * @param entCode
     * @param deviceIds :����ն�
     * @param deviceIds :����
     * @return
     */
    List<EmployeeAttend> getEmployeeAttendByCondition(String entCode,String deviceIds,String attendDate);

    /**
     * ͨ�����������ղ����Ա���Ŀ�����Ϣ
     * @param entCode
     * @param deviceId :�����ն�
     * @param deviceIds :����
     * @return
     */
   public EmployeeAttend getAttendStates(Integer attendStates, String deviceId, String entCode);

   /**
    * ͨ����ǰ�����ж���֮ǰ�������������û�����
    * �������,��ȡ���ʱ��+��ٵ�ʱ�� >=��ǰʱ�� ����û�����
    * @param deviceId
    * @param entCode
    * @param attendanceDate
    * @param attendStates
    */
   public boolean judgeVacate(String deviceId, String entCode, Integer attendanceDate, int attendStates);
   
   /**
    * ��ȡ��˾���������
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
    * ͨ��Id��ȡ����Ա���Ŀ���
    * @param empAtteId
    * @return
    */
   public EmployeeAttend getEmployeeAttendById(Long empAtteId);
   
   /**
    * ͨ����ҵ���룬�ն�ID���������ڣ�����״̬��ȡ����
    * @param entCode
    * @param deviceId
    * @param attendDate
    * @param states
    * @return
    */
   public EmployeeAttend getEmployeeAttendByDeviceIdAndDateAndStates(String entCode, String deviceId,
        Integer attendDate, Integer states);


}

package com.sosgps.v21.target.service;

import java.util.List;
import java.util.Map;

import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.system.common.UserInfo;

public interface TargetService {

	List<Kpi> getKpi(String entCode, int type);

	List<Kpi> getKpi(String entCode, String type);

	void addKpi(List<Kpi> kpiList, TUser operator);

	void updateKpi(List<Kpi> kpiList, TUser operator);

	TTerminal getTerminal(String deviceId);

	List getTerminalAndGroup(String entCode);

	TTermGroup getTermGroup(Long groupId);

	Map<String, TargetTemplate> getTarget(String entCode, int type, int year);

	List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year, int targetOn, int start, int limit);

	int getTargetCount(String entCode, String vehicleNumber, int type,
			int year, int targetOn);

	List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year,  int start, int limit);
	
	/**
	 * ��ȡ��˾�����ָ�꣨��ҳ��
	 * @param entCode
	 * @param type
	 * @param year
	 * @param start
	 * @param limit
	 * @return
	 */
	List getEntTarget(String entCode,int type, int year,int start, int limit); 
	
	/**
	 * ��ȡ��˾�����ָ��
	 * @param entCode
	 * @param type
	 * @param year
	 * @param start
	 * @param limit
	 * @return
	 */
	Map<String,Object[]> getEntTarget(String entCode,int type, int year); 
			
	int getTargetCount(String entCode, String vehicleNumber, int type,
			int year);
	
	/**
	 * ��ȡ��˾ָ�������
	 * @param entCode
	 * @param type
	 * @param year
	 * @return
	 */
	int getEntTargetCount(String entCode, int type,
			int year);
	
	void importTarget(String entCode, int type, int year,
			List<TargetTemplate> targetList, TUser operator);

	void updateTarget(List<TargetTemplate> targetList, TUser operator);
    
	/**
	 * ��ȡһ����ҵ����˾�����������Ա������
	 * @param entCode
	 * @return
	 */
	int getStaffCount(String entCode);
    
	/**
	 * ��ȡĳ����ҵĳ�����µ�ĳ���·ݣ�Ѯ���ܣ���ָ��
	 * @param entCode
	 * @param targetOn
	 * @param type
	 * @param year
	 * @return
	 */
	List<TargetTemplate> getTarget(String entCode, Integer targetOn,
			Integer type, Integer year);
    
	/**
     * ������õ�ֵ���䵽ÿ��Ա���浽���ݿ�
     * @param avgTargets
     * @author wangzhen
     */
	void updateEntTarget(List<TargetTemplate> avgTargets, TUser user);
     
	/**
	 * 
	 * @param entCode
	 * @return
	 * @author wangzhen
	 */
	List<TargetTemplate> getTarget(String entCode);

    /**
     * ����ָ����������
     * @param entCode
     * @param type
     * @param year
     * @param targetOn
     * @param noTargets
     * @return
     */
	List<TargetTemplate> fillTarget(String entCode, Integer type, Integer year,
			Integer targetOn, List<TargetTemplate> noTargets);

	//String getV21Main(UserInfo userInfo);
}

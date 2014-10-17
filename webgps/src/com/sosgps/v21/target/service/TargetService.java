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
	 * 获取公司级别的指标（分页）
	 * @param entCode
	 * @param type
	 * @param year
	 * @param start
	 * @param limit
	 * @return
	 */
	List getEntTarget(String entCode,int type, int year,int start, int limit); 
	
	/**
	 * 获取公司级别的指标
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
	 * 获取公司指标的总数
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
	 * 获取一个企业（公司）下面的所有员工总数
	 * @param entCode
	 * @return
	 */
	int getStaffCount(String entCode);
    
	/**
	 * 获取某个企业某个年下的某个月份（旬、周）的指标
	 * @param entCode
	 * @param targetOn
	 * @param type
	 * @param year
	 * @return
	 */
	List<TargetTemplate> getTarget(String entCode, Integer targetOn,
			Integer type, Integer year);
    
	/**
     * 将分配好的值分配到每个员工存到数据库
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
     * 补齐指标其他参数
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

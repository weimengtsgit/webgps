package com.sosgps.v21.dao;

import java.util.List;

import com.sosgps.v21.model.Kpi;
import com.sosgps.v21.model.TargetTemplate;
import com.sosgps.wzt.orm.TTerminal;

public interface TargetDao {

	List<Kpi> getKpi(String entCode, int type);

	List<Kpi> getKpi(String entCode, String type);

	void addKpi(Kpi kpi);

	void updateKpi(Kpi kpi);

	List getTerminalAndGroup(String entCode);

	List<TargetTemplate> getTarget(String entCode, int type, int year);

	int getTargetCount(String entCode);

	int getTargetCount(String entCode, int type);

	int getTargetCount(String entCode, int type, int year);

	void deleteTarget(String entCode);

	void deleteTarget(String entCode, String deviceId, int type, int year,
			int targetOn);

	void addTarget(List<TargetTemplate> targetList);

	List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year, int targetOn, int start, int limit);

	int getTargetCount(String entCode, String vehicleNumber, int type,
			int year, int targetOn);

	List<TargetTemplate> getTarget(String entCode, String vehicleNumber,
			int type, int year, int start, int limit);
	
	/**
	 * 得到企业级别的指标(分页)
	 * @param entCode
	 * @param type
	 * @param year
	 * @param start
	 * @param limit
	 * @return
	 */
	List getEntTarget(String entCode,
			int type, int year, int start, int limit);
	
	/**
	 * 得到企业级别的指标
	 * @param entCode
	 * @param type
	 * @param year
	 * @param start
	 * @param limit
	 * @return
	 */
	List getEntTarget(String entCode,
			int type, int year);

	int getTargetCount(String entCode, String vehicleNumber, int type, int year);

	void updateTarget(TargetTemplate target);

	void deleteKpi(String entCode);

	void executeSql(String sql);
    
	/**
	 * 获取一个公司下的员工总数
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
	 * 获取企业级别下的所有符合条件的总数
	 * @param entCode
	 * @param type
	 * @param year
	 * @return
	 */
	int getEntTargetCount(String entCode, int type, int year);

	
    /**
     * 删掉以下符合条件的指标
     * @param entCode
     * @param type
     * @param year
     * @param targetOn
     * @author wangzhen
     */
	void deleteTarget(String entCode, Integer type, Integer year,
			Integer targetOn);

	List<TTerminal> getTerminal(String entCode);
    /**
     * 添加企业指标
     * @param target
     * @author wangzhen
     */
	void addAntTarget(List<TargetTemplate> targets);
}

package com.sosgps.wzt.manage.terminalgroup.dao;

import java.util.List;

import com.sosgps.wzt.orm.TTermGroup;

/**
 * @Title:终端组管理数据层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 上午11:45:07
 */
public interface TerminalGroupManageDao {
	public void save(TTermGroup transientInstance);

	public void delete(TTermGroup persistentInstance);

	public TTermGroup findById(Long id);
	
	public void attachDirty(TTermGroup instance);
	/**
	 * 用户可见终端组+终端sos
	 * @param userId
	 * @param empCode
	 * @param node
	 * @return
	 */
	public List viewUserTermGroup(Long userId,String empCode,String node);
	/**
	 * 用户可见终端组
	 * 
	 * @param userId
	 * @return
	 */
	public List viewUserTermGroup(Long userId,String empCode);

	/**
	 * 查看企业终端组
	 * 
	 * @param entCode
	 * @return
	 */
	public List viewEntTermGroup(String entCode);
}

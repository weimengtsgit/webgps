package com.sosgps.wzt.manage.terminal.service;

import java.util.List;

import com.sosgps.wzt.orm.TEnt;

/**
 * @Title:终端管理业务层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 下午05:36:19
 */
public interface TerminalManageService {
	public boolean add();

	public boolean edit();

	public boolean delete();

	public List findByEntId(String entCode);
	/**
	 * 根据组id查找终端sos
	 * @param termGroupId
	 * @return
	 */
	public String findByTermGroupId(Long termGroupId, TEnt tEnt) ;
	/**
	 * 通过终端组id查找组下终端列表
	 * 
	 * @param termGroupId
	 * @return
	 */
	public String findByTermGroupId(Long termGroupId, String action ,String isOndblclick);
	/**
	 * 通过终端组id查找组下个别终端列表
	 * 
	 * @param termGroupId
	 * @return
	 */
	public String findSpecialByTermGroupId(Long termGroupId, String type, String action ,String isOndblclick);
	
	public boolean updateRefTermGroup(String[] deviceIds, String groupId);
	
	/**
	 * 通过终端组id数组查找组下终端列表
	 * @param termGroupIds
	 * @return
	 */
	public List findByTermGroupIds(Long[] termGroupIds);
	public String findByTermGroupIdNoCheck(Long termGroupId) ;
}

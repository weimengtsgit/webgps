package com.sosgps.wzt.manage.terminal.dao;

import java.util.List;

import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TTerminal;

/**
 * @Title:终端管理数据层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 下午05:38:43
 */
public interface TerminalManageDao {
	public boolean add();

	public boolean edit();

	public boolean delete();

	public List findByEntId();

	public List findByEntCode(Object entCode);
	/**
	 * 通过终端组id查找组下终端列表
	 * 
	 * @param termGroupId
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId);
	
	/**
	 * 通过终端组id查找组下是否分配的终端列表
	 * 
	 * @param termGroupId
	 * @param isAllocate
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId, String isAllocate);

	/**
	 * 通过deviceId查找最后一条上传数据
	 * @param deviceId
	 * @return
	 */
	public TLastLocrecord findByLastLocrecord(String deviceId);
	
	/**
	 * 通过终端组id查找组下是否分配的个别终端列表
	 * 
	 * @param termGroupId
	 * @param type
	 * @param isAllocate
	 * @return
	 */
	public List findSpecialByTermGroupId(Long termGroupId, String type,
			String isAllocate);

	/**
	 * 通过终端组id数组查找组下终端列表
	 * 
	 * @param termGroupIds
	 * @return
	 */
	public List findByTermGroupIds(Long[] termGroupIds) throws Exception;
	/**
	 * 根据deviceId查询终端到期时间
	 * @param deviceId
	 * @return
	 */
	public TTerminal findTerminalById(String deviceId);
}

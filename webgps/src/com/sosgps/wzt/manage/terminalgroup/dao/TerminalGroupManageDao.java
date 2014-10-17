package com.sosgps.wzt.manage.terminalgroup.dao;

import java.util.List;

import com.sosgps.wzt.orm.TTermGroup;

/**
 * @Title:�ն���������ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����11:45:07
 */
public interface TerminalGroupManageDao {
	public void save(TTermGroup transientInstance);

	public void delete(TTermGroup persistentInstance);

	public TTermGroup findById(Long id);
	
	public void attachDirty(TTermGroup instance);
	/**
	 * �û��ɼ��ն���+�ն�sos
	 * @param userId
	 * @param empCode
	 * @param node
	 * @return
	 */
	public List viewUserTermGroup(Long userId,String empCode,String node);
	/**
	 * �û��ɼ��ն���
	 * 
	 * @param userId
	 * @return
	 */
	public List viewUserTermGroup(Long userId,String empCode);

	/**
	 * �鿴��ҵ�ն���
	 * 
	 * @param entCode
	 * @return
	 */
	public List viewEntTermGroup(String entCode);
}

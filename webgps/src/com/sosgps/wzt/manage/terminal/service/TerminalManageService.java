package com.sosgps.wzt.manage.terminal.service;

import java.util.List;

import com.sosgps.wzt.orm.TEnt;

/**
 * @Title:�ն˹���ҵ���ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����05:36:19
 */
public interface TerminalManageService {
	public boolean add();

	public boolean edit();

	public boolean delete();

	public List findByEntId(String entCode);
	/**
	 * ������id�����ն�sos
	 * @param termGroupId
	 * @return
	 */
	public String findByTermGroupId(Long termGroupId, TEnt tEnt) ;
	/**
	 * ͨ���ն���id���������ն��б�
	 * 
	 * @param termGroupId
	 * @return
	 */
	public String findByTermGroupId(Long termGroupId, String action ,String isOndblclick);
	/**
	 * ͨ���ն���id�������¸����ն��б�
	 * 
	 * @param termGroupId
	 * @return
	 */
	public String findSpecialByTermGroupId(Long termGroupId, String type, String action ,String isOndblclick);
	
	public boolean updateRefTermGroup(String[] deviceIds, String groupId);
	
	/**
	 * ͨ���ն���id������������ն��б�
	 * @param termGroupIds
	 * @return
	 */
	public List findByTermGroupIds(Long[] termGroupIds);
	public String findByTermGroupIdNoCheck(Long termGroupId) ;
}

package com.sosgps.wzt.manage.terminal.dao;

import java.util.List;

import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TTerminal;

/**
 * @Title:�ն˹������ݲ�ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����05:38:43
 */
public interface TerminalManageDao {
	public boolean add();

	public boolean edit();

	public boolean delete();

	public List findByEntId();

	public List findByEntCode(Object entCode);
	/**
	 * ͨ���ն���id���������ն��б�
	 * 
	 * @param termGroupId
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId);
	
	/**
	 * ͨ���ն���id���������Ƿ������ն��б�
	 * 
	 * @param termGroupId
	 * @param isAllocate
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId, String isAllocate);

	/**
	 * ͨ��deviceId�������һ���ϴ�����
	 * @param deviceId
	 * @return
	 */
	public TLastLocrecord findByLastLocrecord(String deviceId);
	
	/**
	 * ͨ���ն���id���������Ƿ����ĸ����ն��б�
	 * 
	 * @param termGroupId
	 * @param type
	 * @param isAllocate
	 * @return
	 */
	public List findSpecialByTermGroupId(Long termGroupId, String type,
			String isAllocate);

	/**
	 * ͨ���ն���id������������ն��б�
	 * 
	 * @param termGroupIds
	 * @return
	 */
	public List findByTermGroupIds(Long[] termGroupIds) throws Exception;
	/**
	 * ����deviceId��ѯ�ն˵���ʱ��
	 * @param deviceId
	 * @return
	 */
	public TTerminal findTerminalById(String deviceId);
}

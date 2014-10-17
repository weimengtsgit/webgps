package com.sosgps.wzt.manage.terminalgroup.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TUser;

/**
 * @Title:�û��ɼ��ն������ҵ���ӿ�
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����12:43:50
 */
public interface TerminalGroupManageService {
	/**
	 * �û��ɼ��ն���+�ն�
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

	/**
	 * �����ն�������
	 * 
	 * @param entCode
	 * @param termGroupName
	 * @param termGroupId
	 * @param childrenGroupMaxId
	 * @return
	 */
	public boolean add(String entCode, String termGroupName,
			String termGroupId, String childrenGroupMaxId);

	/**
	 * �޸��ն���
	 * 
	 * @param termGroupId
	 * @param termGroupName
	 * @return
	 */
	public boolean edit(String termGroupId, String termGroupName);

	/**
	 * ɾ���ն���
	 * 
	 * @param termGroupId
	 * @return
	 */
	public boolean delete(String termGroupId);

	/**
	 * ������ҵ�û��б�
	 * 
	 * @param entCode
	 * @param pageNo
	 * @param pageSize
	 * @param paramName
	 * @param paramValue
	 * @param vague
	 * @return
	 */
	public Page<TUser> listUser(String entCode, String pageNo, String pageSize,
			String paramName, String paramValue, String vague);

	/**
	 * �����û��ɼ��ն���
	 * 
	 * @param entCode
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	public boolean userTermGroupSet(String entCode, Long userId, String groupIds);

	/**
	 * �ն���������ϵ����
	 * 
	 * @param groupId
	 * @param parentGroupId
	 * @return
	 */
	public boolean editGroupInGroup(String groupId, String parentGroupId);
	// sosɾ���û��ɼ�������
	public boolean deleteUserTermGroup(Long userId);
	// sos�����û��ɼ���
	public boolean addUserTermGroup(String entCode, Long userId, Long groupId);
}

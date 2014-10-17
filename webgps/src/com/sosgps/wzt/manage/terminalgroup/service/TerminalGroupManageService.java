package com.sosgps.wzt.manage.terminalgroup.service;

import java.util.List;

import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TUser;

/**
 * @Title:用户可见终端组管理业务层接口
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 下午12:43:50
 */
public interface TerminalGroupManageService {
	/**
	 * 用户可见终端组+终端
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

	/**
	 * 增加终端下属组
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
	 * 修改终端组
	 * 
	 * @param termGroupId
	 * @param termGroupName
	 * @return
	 */
	public boolean edit(String termGroupId, String termGroupName);

	/**
	 * 删除终端组
	 * 
	 * @param termGroupId
	 * @return
	 */
	public boolean delete(String termGroupId);

	/**
	 * 查找企业用户列表
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
	 * 设置用户可见终端组
	 * 
	 * @param entCode
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	public boolean userTermGroupSet(String entCode, Long userId, String groupIds);

	/**
	 * 终端组所属关系调整
	 * 
	 * @param groupId
	 * @param parentGroupId
	 * @return
	 */
	public boolean editGroupInGroup(String groupId, String parentGroupId);
	// sos删除用户可见组设置
	public boolean deleteUserTermGroup(Long userId);
	// sos增加用户可见组
	public boolean addUserTermGroup(String entCode, Long userId, Long groupId);
}

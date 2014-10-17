package com.sosgps.wzt.manage.terminalgroup.service.impl;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminalgroup.dao.TerminalGroupManageDao;
import com.sosgps.wzt.manage.terminalgroup.dao.UserDao;
import com.sosgps.wzt.manage.terminalgroup.dao.UserTerminalGroupManageDao;
import com.sosgps.wzt.manage.terminalgroup.service.TerminalGroupManageService;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.RefTermGroup;
import com.sosgps.wzt.orm.RefTermGroupId;
import com.sosgps.wzt.orm.RefUserTgroup;
import com.sosgps.wzt.orm.RefUserTgroupId;
import com.sosgps.wzt.orm.TEnt;
import com.sosgps.wzt.orm.TTermGroup;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TUser;
import com.mapinfo.spatialj.geom.mi.pa;
import com.mapinfo.util.gr;

/**
 * @Title:�û��ɼ��ն������ҵ���ӿھ���ʵ����
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����12:45:12
 */
public class TerminalGroupManageServiceImpl implements
		TerminalGroupManageService {
	private TerminalGroupManageDao terminalGroupManageDao;// �ն���������ݲ�ӿ�ʵ��
	private UserTerminalGroupManageDao userTerminalGroupManageDao;// �û��鿴�ն������ݲ�ӿ�ʵ��
	private UserDao userDao;// �û����ݲ�ӿ�ʵ��

	public UserTerminalGroupManageDao getUserTerminalGroupManageDao() {
		return userTerminalGroupManageDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setUserTerminalGroupManageDao(
			UserTerminalGroupManageDao userTerminalGroupManageDao) {
		this.userTerminalGroupManageDao = userTerminalGroupManageDao;
	}

	/**
	 * ����ĳ���parentIdС���丸���parentId�������������(��������ڸ���֮ǰ)������ʹ�������������֮ǰ
	 * 
	 * @param c
	 * @param index
	 */
	// �磺
	// id parentid sort
	// 5 10 1
	// 10 11 1
	// �˷������Ե������򽫸�����������ǰ�棺
	// id parentid sort
	// 10 11 1
	// 5 10 1
	public void helpSortTermGroup(List c, int index, Hashtable<Long, Long> ids) {
		// �����Ѿ��鵽��id
		if (ids == null)
			ids = new Hashtable<Long, Long>();
		if (index == -1) {
			int theIndex = -1;
			for (int i = 0; i < c.size(); i++) {
				TTermGroup g = (TTermGroup) c.get(i);
				if (ids.get(g.getId()) == null) {
					ids.put(g.getId(), g.getId());// ��id���뼯��
					if (g.getParentId() != -1
							&& ids.get(g.getParentId()) == null) {// ��id�ڼ�����δ����
						theIndex = i;
						break;
					}
				}
			}
			if (theIndex != -1) {
				helpSortTermGroup(c, theIndex, ids);// �ݹ�
			}
		} else if (index < c.size()) {
			TTermGroup gg = (TTermGroup) c.get(index);
			long id = gg.getParentId();
			// ���Ҵ�parentid��Ӧ��id��list�е�λ��
			int parentIndex = -1;
			for (int i = index; i < c.size(); i++) {
				TTermGroup g = (TTermGroup) c.get(i);
				if (g.getId() == id) {
					parentIndex = i;
					break;
				}
			}
			// ��parentIndex��Ԫ��ɾ���������뵽index��ǰ��
			if (parentIndex > index) {
				TTermGroup parentTTermGroup = (TTermGroup) c.get(parentIndex);
				int nextIndex = index;
				int indexNext = index;
				if (parentTTermGroup.getGroupSort() != 1) {
					for (int i = index; i <= parentIndex; i++) {
						int tempIndex = i;
						TTermGroup tempParentTTermGroup = (TTermGroup) c
								.get(tempIndex);
						if (tempParentTTermGroup.getParentId().intValue() == parentTTermGroup
								.getParentId().intValue()) {
							c.remove(tempIndex);
							c.add(indexNext, tempParentTTermGroup);
							indexNext++;
						}
					}
				} else {
					c.remove(parentIndex);
					c.add(index, parentTTermGroup);
					indexNext++;
				}
				int parentIndexNext = parentIndex + 1;
				if (parentIndexNext < c.size()) {
					TTermGroup tempParentTTermGroup = (TTermGroup) c
							.get(parentIndexNext);
					while (parentIndexNext < c.size()
							&& tempParentTTermGroup.getParentId().intValue() == parentTTermGroup
									.getParentId().intValue()) {
						c.remove(tempParentTTermGroup);
						c.add(indexNext, tempParentTTermGroup);
						indexNext++;
						parentIndexNext++;
						if (parentIndexNext < c.size())
							tempParentTTermGroup = (TTermGroup) c
									.get(parentIndexNext);
						else
							break;
					}
				}
				helpSortTermGroup(c, nextIndex, ids);// �ݹ�
			} else {
				helpSortTermGroup(c, -1, ids);
			}
		}
	}
	/**
	 * �û��ɼ��ն���+�ն�sos
	 */
	public List viewUserTermGroup(Long userId, String empCode,String node) {
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		List userViewGroups = null;
		try {
			userViewGroups = terminalGroupManageDao.viewUserTermGroup(userId,
					empCode,node);
			helpSortTermGroup(userViewGroups, -1, null);
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_USER_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return userViewGroups;
	}

	public List viewUserTermGroup(Long userId, String empCode) {
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		List userViewGroups = null;
		try {
			userViewGroups = terminalGroupManageDao.viewUserTermGroup(userId,
					empCode);
			helpSortTermGroup(userViewGroups, -1, null);
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_USER_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return userViewGroups;
	}

	public TerminalGroupManageDao getTerminalGroupManageDao() {
		return terminalGroupManageDao;
	}

	public void setTerminalGroupManageDao(
			TerminalGroupManageDao terminalGroupManageDao) {
		this.terminalGroupManageDao = terminalGroupManageDao;
	}

	/**
	 * �鿴��ҵ�ն���
	 * 
	 * @param entCode
	 * @return
	 */
	public List viewEntTermGroup(String entCode) {
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return null;
		}
		List viewEntTermGroup = null;
		try {
			viewEntTermGroup = terminalGroupManageDao.viewEntTermGroup(entCode);
			helpSortTermGroup(viewEntTermGroup, -1, null);
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_ENT_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return viewEntTermGroup;
	}

	/**
	 * �����ն���
	 * 
	 * @param entCode
	 * @param termGroupName
	 * @param termGroupId
	 * @param childrenGroupMaxId
	 * @return
	 */
	public boolean add(String entCode, String termGroupName,
			String termGroupId, String childrenGroupMaxId) {
		boolean re = false;
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		TTermGroup termGroup = new TTermGroup();
		termGroup.setGroupName(termGroupName);
		Long groupSort = 0L;
		Long parentId = 0L;
		try {
			parentId = Long.parseLong(termGroupId);
			groupSort = Long.parseLong(childrenGroupMaxId) + 1;
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_ENT + " "
					+ Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
			return false;
		}

		termGroup.setParentId(parentId);
		termGroup.setGroupSort(groupSort);
		TEnt TEnt = new TEnt();
		TEnt.setEntCode(entCode);
		termGroup.setTEnt(TEnt);
		try {
			terminalGroupManageDao.save(termGroup);
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ADD_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return re;
	}

	/**
	 * ɾ���ն���
	 * 
	 * @param termGroupId
	 * @return
	 */
	public boolean delete(String termGroupId) {
		boolean re = false;
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		Long id = 0L;
		try {
			id = Long.parseLong(termGroupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_ENT + " "
					+ Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
			return false;
		}
		try {
			TTermGroup termGroup = terminalGroupManageDao.findById(id);
			terminalGroupManageDao.delete(termGroup);
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.DELETE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return re;
	}

	/**
	 * �޸��ն���
	 * 
	 * @param termGroupId
	 * @param termGroupName
	 * @return
	 */
	public boolean edit(String termGroupId, String termGroupName) {
		boolean re = false;
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		Long id = 0L;
		try {
			id = Long.parseLong(termGroupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_ENT + " "
					+ Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
			return false;
		}
		try {
			TTermGroup termGroup = terminalGroupManageDao.findById(id);
			termGroup.setGroupName(termGroupName);
			terminalGroupManageDao.attachDirty(termGroup);
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.EDIT_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO, e);
		}
		return re;
	}

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
			String paramName, String paramValue, String vague) {
		Page<TUser> re = null;
		if (userDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		re = new Page<TUser>();
		int page = 1;
		int pageS = 10;
		if (pageNo == null) {
			pageNo = "1";
		}
		if (pageSize == null) {
			pageSize = "10";
		}
		try {
			page = Integer.parseInt(pageNo);
			pageS = Integer.parseInt(pageSize);
		} catch (Exception e) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_ENT + " "
					+ Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
			return re;
		}
		try {
			re.setAutoCount(true);
			re.setPageNo(page);
			re.setPageSize(pageS);
			String hql = "select t from TUser t where t.empCode='" + entCode
					+ "'";
			if (paramName != null && !paramName.equals("")) {
				if (vague == null || vague.equalsIgnoreCase("y")) {
					hql += " and " + paramName + " like ?";
					re = userDao.findByPage(re, hql, "%" + paramValue + "%");
				} else {
					hql += " and " + paramName + "=?";
					re = userDao.findByPage(re, hql, paramValue);
				}
			} else {
				re = userDao.findByPage(re, hql);
			}
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.USER_LIST, e);
		}
		return re;
	}

	/**
	 * �����û��ɼ��ն���
	 * 
	 * @param entCode
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	public boolean userTermGroupSet(String entCode, Long userId, String groupIds) {
		boolean re = false;
		if (userTerminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		if (groupIds == null) {
			return re;
		}
		StringTokenizer st = new StringTokenizer(groupIds, ",");
		HashMap<Long, Long> ids = new HashMap<Long, Long>();
		for (; st.hasMoreElements();) {
			try {
				String gId = (String) st.nextElement();
				Long id = Long.parseLong(gId);
				ids.put(id, id);
			} catch (Exception e) {
				SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_ENT
						+ " " + Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
				return false;
			}
		}
		try {
			// ���Ҵ��û��¹�����ն�����Ϣ
			List refUtgs = userTerminalGroupManageDao.findByProperty(
					"id.userId", userId);
			if (refUtgs == null || refUtgs.size() == 0) {
				// ���ӹ����ն�����Ϣ
				for (Iterator it = ids.keySet().iterator(); it.hasNext();) {
					Long key = (Long) it.next();
					RefUserTgroup transientInstance = new RefUserTgroup();
					RefUserTgroupId refId = new RefUserTgroupId(userId);
					TTermGroup g = new TTermGroup();
					g.setId(ids.get(key));
					refId.setTTermGroup(g);
					transientInstance.setId(refId);
					userTerminalGroupManageDao.save(transientInstance);
				}
			} else {
				for (Iterator it = refUtgs.iterator(); it.hasNext();) {
					RefUserTgroup refUtg = (RefUserTgroup) it.next();
					Long gId = refUtg.getTTermGroup().getId();// �ն���id
					// ���û�У�ɾ��
					if (ids.get(gId) == null) {
						userTerminalGroupManageDao.delete(refUtg);
					}
					// ����У���ids��ɾ��
					else {
						ids.remove(gId);
					}
				}
				// ids��ʣ�µ�Ϊ�����ģ�����
				for (Iterator it = ids.keySet().iterator(); it.hasNext();) {
					Long key = (Long) it.next();
					RefUserTgroup transientInstance = new RefUserTgroup();
					RefUserTgroupId refId = new RefUserTgroupId(userId);
					TTermGroup g = new TTermGroup();
					g.setId(ids.get(key));
					refId.setTTermGroup(g);
					transientInstance.setId(refId);
					userTerminalGroupManageDao.save(transientInstance);
				}
			}
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.SET_USER_TERM_GROUP, e);
		}
		return re;
	}

	/**
	 * �ն���������ϵ����
	 * 
	 * @param groupId
	 * @param parentGroupId
	 * @return
	 */
	public boolean editGroupInGroup(String groupId, String parentGroupId) {
		boolean re = false;
		if (terminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		Long gId = 0L;
		Long pId = 0L;
		try {
			gId = Long.parseLong(groupId);
			pId = Long.parseLong(parentGroupId);
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
		}
		try {
			TTermGroup instance = terminalGroupManageDao.findById(gId);
			if (!String.valueOf(instance.getParentId()).equals(parentGroupId)) {
				instance.setParentId(pId);
				terminalGroupManageDao.attachDirty(instance);
			}
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP, e);
		}
		return re;
	}

	public boolean deleteUserTermGroup(Long userId) {
		List userTermGroups = userTerminalGroupManageDao.findByProperty("id.userId", userId);
		if(userTermGroups!=null){
			for (Iterator it = userTermGroups.iterator(); it.hasNext();) {
				RefUserTgroup refUserTgroup = (RefUserTgroup)it.next();
				userTerminalGroupManageDao.delete(refUserTgroup);
			}
		}
		return true;
	}
	
	public boolean addUserTermGroup(String entCode, Long userId, Long groupId) {
		boolean re = false;
		if (userTerminalGroupManageDao == null) {
			SysLogger.sysLogger
					.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.ERROR_DAO_INSTANCE_NULL);
			return re;
		}
		try {
			// ���ӹ����ն�����Ϣ
			RefUserTgroup transientInstance = new RefUserTgroup();
			RefUserTgroupId refId = new RefUserTgroupId(userId);
			TTermGroup g = new TTermGroup();
			g.setId(groupId);
			refId.setTTermGroup(g);
			transientInstance.setId(refId);
			userTerminalGroupManageDao.save(transientInstance);
			re = true;
		} catch (Exception e) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.SET_USER_TERM_GROUP, e);
		}
		return re;
	}
}
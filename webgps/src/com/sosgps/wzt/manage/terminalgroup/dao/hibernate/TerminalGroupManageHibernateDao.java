package com.sosgps.wzt.manage.terminalgroup.dao.hibernate;

import java.util.List;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminalgroup.dao.TerminalGroupManageDao;
import com.sosgps.wzt.orm.TTermGroupDAO;

/**
 * @Title:终端组管理数据层接口具体实现类
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 下午01:58:02
 */
public class TerminalGroupManageHibernateDao extends TTermGroupDAO implements
		TerminalGroupManageDao {
	/**
	 * 用户可见终端组+终端sos
	 */
	public List viewUserTermGroup(Long userId,String empCode,String node) {
		try {
			String queryString = "select model from TTermGroup model join model.refUserTgroups rut where rut.id.userId="
					+ userId + " and model.TEnt.entCode='"+empCode+"' and model.parentId ="+node+"  order by model.parentId,model.groupSort";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_USER_TERMINAL_GROUP + " userId="
							+ userId, re);
			throw re;
		}
	}
	
	/**
	 * 用户可见终端组
	 */
	public List viewUserTermGroup(Long userId,String empCode) {
		try {
			String queryString = "select model from TTermGroup model join model.refUserTgroups rut where rut.id.userId="
					+ userId + " and model.TEnt.entCode='"+empCode+"' order by model.parentId,model.groupSort";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_USER_TERMINAL_GROUP + " userId="
							+ userId, re);
			throw re;
		}
	}

	/**
	 * 查看企业终端组
	 * 
	 * @param entCode
	 * @return
	 */
	public List viewEntTermGroup(String entCode) {
		try {
			String queryString = "select model from TTermGroup model where model.TEnt.entCode='"
					//+ entCode + "' and model.groupStatus = 1 order by model.parentId,model.groupSort";
				+ entCode + "'  order by model.parentId,model.groupSort";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(
					LogConstants.LOG_SOURCE_MANAGE_TERMINAL_GROUP + " "
							+ Constants.VIEW_ENT_TERMINAL_GROUP + " entCode="
							+ entCode, re);
			throw re;
		}
	}
}

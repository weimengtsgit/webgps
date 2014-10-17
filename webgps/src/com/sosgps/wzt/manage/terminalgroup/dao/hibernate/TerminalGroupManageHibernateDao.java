package com.sosgps.wzt.manage.terminalgroup.dao.hibernate;

import java.util.List;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminalgroup.dao.TerminalGroupManageDao;
import com.sosgps.wzt.orm.TTermGroupDAO;

/**
 * @Title:�ն���������ݲ�ӿھ���ʵ����
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����01:58:02
 */
public class TerminalGroupManageHibernateDao extends TTermGroupDAO implements
		TerminalGroupManageDao {
	/**
	 * �û��ɼ��ն���+�ն�sos
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
	 * �û��ɼ��ն���
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
	 * �鿴��ҵ�ն���
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

package com.sosgps.wzt.manage.terminal.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.common.LogConstants;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.terminal.dao.TerminalManageDao;
import com.sosgps.wzt.orm.TLastLocrecord;
import com.sosgps.wzt.orm.TTerminal;
import com.sosgps.wzt.orm.TTerminalDAO;

/**
 * @Title:�ն˹������ݲ�ӿھ���ʵ����
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-1 ����05:39:32
 */
public class TerminalManageHibernateDao extends TTerminalDAO implements
		TerminalManageDao {

	public boolean add() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean edit() {
		// TODO Auto-generated method stub
		return false;
	}

	public List findByEntId() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ͨ���ն���id���������ն��б�
	 * 
	 * @param termGroupId
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId) {
		try {
			String queryString = "select model from TTerminal model join model.refTermGroups rtg where rtg.TTermGroup.id="
					+ termGroupId + " order by model.termName";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + "�ն���ID="
					+ termGroupId, re);
			throw re;
		}
	}

	/**
	 * ͨ���ն���id���������Ƿ������ն��б�
	 * 
	 * @param termGroupId
	 * @param isAllocate
	 * @return
	 */
	public List findByTermGroupId(Long termGroupId, String isAllocate) {
		try {
			String queryString = "select model from TTerminal model join model.refTermGroups rtg where rtg.TTermGroup.id="
					+ termGroupId + " and model.isAllocate=" + isAllocate+ " order by model.termName";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + "�ն���ID="
					+ termGroupId + " �Ƿ����=" + isAllocate, re);
			throw re;
		}
	}
	/**
	 * ͨ��deviceId�������һ���ϴ�����
	 */
	public TLastLocrecord findByLastLocrecord(String deviceId) {
			String queryString = "from TLastLocrecord as tl where tl.deviceId='"
					+ deviceId+"' and tl.latitude>0 and tl.longitude>0";
			List list=getHibernateTemplate().find(queryString);
			if(list.size()>0){
				TLastLocrecord tl = (TLastLocrecord)list.get(0);
				return tl;
			}
			return  null;
	}

	/**
	 * ͨ���ն���id���������Ƿ������ն��б�
	 * 
	 * @param termGroupId
	 * @param type
	 * @param isAllocate
	 * @return
	 */
	public List findSpecialByTermGroupId(Long termGroupId, String type,
			String isAllocate) {
		try {
			String queryString = "select model from TTerminal model join model.refTermGroups rtg where rtg.TTermGroup.id="
					+ termGroupId
					+ " and model.isAllocate="
					+ isAllocate
					+ " and model.locateType=" + type+ " order by model.termName";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + "�ն���ID="
					+ termGroupId + " �Ƿ����=" + isAllocate + " ����=" + type, re);
			throw re;
		}
	}

	/**
	 * ͨ���ն���id������������ն��б�
	 * 
	 * @param termGroupIds
	 * @return
	 * @throws Exception
	 */
	public List findByTermGroupIds(final Long[] termGroupIds) throws Exception {
		try {
			return (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							Query query = s
									.createQuery("select model from TTerminal model join model.refTermGroups rtg where rtg.TTermGroup.id in (:parentIds) order by model.termName");
							query.setParameterList("parentIds", termGroupIds);
							return query.list();
						}
					});
		} catch (Exception re) {
			SysLogger.sysLogger.error(LogConstants.LOG_SOURCE_MANAGE_TERMINAL
					+ " " + Constants.FIND_TERMINAL_FROM_GROUP + "�ն���ID="
					+ termGroupIds, re);
			throw re;
		}
	}
	/**
	 * ����deviceId��ѯ�ն˵���ʱ��
	 * @param deviceId
	 * @return
	 */
	public TTerminal findTerminalById(String deviceId){
		String queryString = "from TTerminal as tl where tl.deviceId='"
				+ deviceId+"'";
		List list=getHibernateTemplate().find(queryString);
		if(list.size()>0){
			TTerminal tl = (TTerminal)list.get(0);
			return  tl;
		}
		return  null;
}
}

package com.sosgps.wzt.manage.terminal.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.manage.terminal.dao.RefTermGroupDao;
import com.sosgps.wzt.orm.RefTermGroupDAO;

/**
 * @Title:
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2008-12-30 ÏÂÎç05:26:48
 */
public class RefTermGroupHibernateDao extends RefTermGroupDAO implements
		RefTermGroupDao {

	public void update(final String deviceId, final String groupId) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s
							.createQuery("update RefTermGroup t set t.TTermGroup.id="
									+ groupId
									+ " where t.TTerminal.deviceId='"
									+ deviceId + "'");
					query.executeUpdate();
					return null;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}

	public void deleteAll(final String[] deviceIds) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s
							.createQuery("delete RefTermGroup t where t.id.TTerminal.deviceId in (:ids)");
					query.setParameterList("ids", deviceIds);
					query.executeUpdate();
					return null;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}
}

package com.sosgps.wzt.struction.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.struction.dao.SpeedCaseDao;
import com.sosgps.wzt.orm.TSpeedCase;
import com.sosgps.wzt.orm.TSpeedCaseDAO;

/**
 * @Title:
 * @Description:
 * @Company: Autonavi
 * @author: jingwei.sun
 * @version 1.0
 * @date: 2009-2-24 ÉÏÎç11:24:59
 */
public class SpeedCaseHibernateDao extends TSpeedCaseDAO implements
		SpeedCaseDao {

	public TSpeedCase findByDeviceId(final String deviceId) {
		try {
			return (TSpeedCase) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session s)
								throws HibernateException, SQLException {
							Query query = s
									.createQuery("select model from TSpeedCase model where model.flag=1 and model.deviceId=:deviceId order by model.crtdate desc");
							query.setParameter("deviceId", deviceId);
							query.setFirstResult(0);
							query.setMaxResults(1);
							return query.uniqueResult();
						}
					});
		} catch (RuntimeException re) {
			throw re;
		}
	}

}

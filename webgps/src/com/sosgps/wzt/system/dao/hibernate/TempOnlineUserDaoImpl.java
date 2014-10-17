/**
 * 
 */
package com.sosgps.wzt.system.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.sosgps.wzt.orm.TempOnlineUserDAO;
import com.sosgps.wzt.system.dao.TempOnlineUserDao;

/**
 * @author xiaojun.luan
 * 
 */
public class TempOnlineUserDaoImpl extends TempOnlineUserDAO implements
		TempOnlineUserDao {

	/**
	 * 
	 */
	public TempOnlineUserDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sosgps.wzt.system.dao.TempOnlineUserDao#deleteAll()
	 */
	public void deleteAll() {
		// TODO Auto-generated method stub
		this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String queryString = "delete from TempOnlineUser";
				session.createQuery(queryString).executeUpdate();
				return null;
			}
		});
	}

}

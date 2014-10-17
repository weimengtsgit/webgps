package com.sosgps.wzt.system.dao.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TUser;
import com.sosgps.wzt.orm.TUserDAO;
import com.sosgps.wzt.system.dao.TUserDao;
import com.sosgps.wzt.util.PageQuery;

public class TUserDaoImpl extends TUserDAO implements TUserDao {
	public boolean deleteAll(Long[] ids) {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		try {
			conn = ((DataSource) SpringHelper.getBean("dataSource"))
					.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			for (int x = 0; x < ids.length; x++) {
				// 删除用户角色表数据

				sql = "delete from ref_user_role t where t.user_id='" + ids[x]
						+ "'";
				st.execute(sql);
				SysLogger.sysLogger.info("I063: 用户管理-正在删除用户角色-用户ID='" + ids[x]
						+ "'");
				// 删除用户表数据
				sql = "delete from t_user t where t.id='" + ids[x] + "'";
				st.execute(sql);
				SysLogger.sysLogger.info("I063: 用户管理-正在删除用户-用户ID='" + ids[x]
						+ "'");

			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I063: 用户管理-删除用户成功 ");
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				ret = false;
				SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", e);

			}
			SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", se);
			ret = false;
		} catch (Exception ex) {
			ret = false;
			SysLogger.sysLogger.error("E063: 用户管理-删除用户失败", ex);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E063: 用户管理-删除用户失败-关闭Statement 失败", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E063: 用户管理-删除用户失败-关闭Connection 失败", e);
				}

			}
		}

		return ret;
	}

	public List findByUserAccount(final String userAccount, final String empCode) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select u from TUser u "
						+ "left join fetch u.refUserRoles ref "
						+ "left join fetch ref.TRole r "
						+ "where u.empCode=:empCode and u.userAccount=:userAccount";
				Query query = arg0.createQuery(hql);
				query.setParameter("empCode", empCode);
				query.setParameter("userAccount", userAccount);
				return query.list();
			}
		});
	}

	public List findByUserName(final String userName, final String empCode) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select u from TUser u "
						+ "left join fetch u.refUserRoles ref "
						+ "left join fetch ref.TRole r "
						+ "where u.empCode=:empCode and u.userName=:userName";
				Query query = arg0.createQuery(hql);
				query.setParameter("empCode", empCode);
				query.setParameter("userName", userName);
				return query.list();
			}
		});
	}

	public TUser findUserAndRoleById(final java.lang.Long id) {
		return (TUser) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select u from TUser u "
						+ "left join fetch u.refUserRoles ref "
						// + "left join fetch ref.TRole r "
						+ "where u.id=:id";
				Query query = arg0.createQuery(hql);
				query.setParameter("id", id);
				query.setFirstResult(0);
				query.setMaxResults(1);
				return query.uniqueResult();
			}
		});
	}

	public List findByUserContact(Object userContact, String empCode) {
		return this.getHibernateTemplate().find(
				"from TUser t where t.userContact='" + userContact
						+ "' and t.empCode='" + empCode + "'");
	}

	/**
	 * 根据empCode查询企业信息。
	 */
	public List queryEntInfoByCode(String empCode) {
		return this.getHibernateTemplate().find(
				"from TEnt t where t.entCode='" + empCode
						+ "' and t.entStatus = 1 ");
	}

	public List queryEntInfoByCode2(String empCode) {
		return this.getHibernateTemplate().find(
				"from TEnt t where t.entCode='" + empCode
						+ "' and (t.entStatus = 1 or t.entStatus = 2)  ");
	}

	public Page<TUser> listUser(String entCode, int startint, int limitint,
			String searchValue) {
		String hql = "select t from TUser t "
				+
				// "left join fetch t.refUserRoles ref " +  
				// "left join fetch ref.TRole r " +
				"where t.empCode='" + entCode
				+ "' and (t.userName like ? or t.userAccount like ?)";
		PageQuery<TUser> pq = new PageQuery<TUser>(this);
		Page<TUser> re = new Page<TUser>();
		re.setAutoCount(true);
		re.setPageNo(startint);
		re.setPageSize(limitint);
		return pq.findByPage(re, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}

	// 任献良
	public Page<TUser> listUserAdmin(String entCode, int startint, int limitint,
			String searchValue) {
		String hql = "select t from TUser t "
				+
				// "left join fetch t.refUserRoles ref "
				// "left join fetch ref.TRole r " +
				"where t.empCode='" + entCode
				+ "' and (t.userName like ? or t.userAccount like ?) order  by t.id";
		PageQuery<TUser> pq = new PageQuery<TUser>(this);
		Page<TUser> re = new Page<TUser>();
		re.setAutoCount(true);
		re.setPageNo(startint);
		re.setPageSize(limitint);
		return pq.findByPage(re, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}
	public List queryUserRolesById(final Long id) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select r from TRole r "
						+ "left join fetch r.refUserRoles ref "
						+ "left join fetch ref.TUser u " + "where u.id=" + id;
				Query query = arg0.createQuery(hql);
				return query.list();
			}
		});
	}

	public List queryUserTgroupsById(final Long id) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select g from RefUserTgroup refg "
						+ "left join refg.TTermGroup g "
						+ "where refg.id.userId=" + id;
				Query query = arg0.createQuery(hql);
				return query.list();
			}
		});
	}

	public TUser findUserByLoginParam(final String empCode,
			final String userAccount, final String userPassword) {
		return (TUser) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select u from TUser u "
						+ "left join fetch u.refUserRoles ref "
						+ "left join fetch ref.TRole r "
						+ "where u.empCode=:empCode and u.userAccount=:userAccount and u.userPassword=:userPassword";
				Query query = arg0.createQuery(hql);
				query.setParameter("empCode", empCode);
				query.setParameter("userAccount", userAccount);
				query.setParameter("userPassword", userPassword);
				query.setFirstResult(0);
				query.setMaxResults(1);
				return query.uniqueResult();
			}
		});

	}
}

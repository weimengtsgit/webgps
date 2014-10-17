package com.sosgps.wzt.system.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TModule;
import com.sosgps.wzt.orm.TModuleDAO;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.bean.ModuleModuleGroupBean;
import com.sosgps.wzt.system.dao.TModuleDao;
import com.sosgps.wzt.util.PageQuery;

public class TModuleDaoImpl extends TModuleDAO implements TModuleDao {
	public TModuleDaoImpl() {
		super();
	}

	public List findByModuleLevel(Long moduleLevel) {
		return this.findByTreeLevel(moduleLevel);
	}

	public List findByModuleGradeList(Long grade) {
		SysLogger.sysLogger.info("I061: 权限管理-获得权限组权限关系");

		try {

			StringBuffer queryStr = new StringBuffer("select ");
			queryStr.append("module,moduleGroup ");
			queryStr.append("from TModule module,TModuleGroup moduleGroup ");
			queryStr.append("where ");
			queryStr
					.append("module.TModuleGroup.id=moduleGroup.id and  module.usageFlag='1' ");
			queryStr.append("and module.moduleGrade='" + grade + "' ");
			queryStr
					.append("order by moduleGroup.groupName,module.moduleGrade ");
			List list = getHibernateTemplate().find(queryStr.toString());
			List results = new ArrayList();
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Object[] obj = (Object[]) iterator.next();
				TModule module = (TModule) obj[0];
				TModuleGroup tModuleGroup = (TModuleGroup) obj[1];
				ModuleModuleGroupBean moduleModuleGroupBean = new ModuleModuleGroupBean();
				moduleModuleGroupBean.setModuleGroupName(tModuleGroup
						.getGroupName());
				moduleModuleGroupBean.setModuleId(module.getId());
				moduleModuleGroupBean.setModuleName(module.getModuleName());
				moduleModuleGroupBean.setModuleDesc(module.getMoudleDesc());
				if (module.getModuleGrade() == 2) {
					moduleModuleGroupBean.setModuleGrade("用户级权限");
				} else {
					moduleModuleGroupBean.setModuleGrade("系统级权限");
				}

				results.add(moduleModuleGroupBean);
			}

			return results;
		} catch (RuntimeException re) {
			SysLogger.sysLogger.error("E061: 权限管理-获得权限组权限关系失败", re);
			throw re;
		}
	}

	public List fingAllModuleList() {
		SysLogger.sysLogger.info("I061: 权限管理-获得权限组权限关系");

		return this.findAll();
	}

	public List fingModuleList(Long[] ids) {
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		String sql = "from TModule  where id in (:ids)";
		Query query = session.createQuery(sql);
		query.setParameterList("ids", ids);
		List list = query.list();
		tx.commit();
		session.close();
		return list;
	}

	public List findByModuleEmpCodeList(String empCode) {

		Connection conn = null;
		Statement st = null;
		List results = new ArrayList();
		StringBuffer sqlb = new StringBuffer();
		sqlb
				.append("select distinct(t.id),t.module_name ,t.moudle_desc,t3.group_name ,t.module_grade ");
		sqlb
				.append("from t_module t,ref_module_role t1 ,t_role t2 ,t_module_group t3 ");
		sqlb.append("where ");
		sqlb
				.append("t1.module_id=t.id and t1.role_id=t2.id and t.module_id=t3.id and t.module_grade=2 ");
		sqlb.append("and t2.emp_code= ");
		sqlb.append("'" + empCode + "' " );
		//sqlb.append("order by t.sorted_index ");
		
		try {
			conn = ((DataSource) SpringHelper.getBean("dataSource"))
					.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = null;
			rs = st.executeQuery(sqlb.toString());
			while (rs.next()) {
				ModuleModuleGroupBean moduleModuleGroupBean = new ModuleModuleGroupBean();
				moduleModuleGroupBean.setModuleGroupName(rs
						.getString("group_name"));
				moduleModuleGroupBean.setModuleId(rs.getLong("id"));
				moduleModuleGroupBean
						.setModuleName(rs.getString("module_name"));
				moduleModuleGroupBean
						.setModuleDesc(rs.getString("moudle_desc"));
				if (rs.getLong("module_grade") == 2) {
					moduleModuleGroupBean.setModuleGrade("用户级权限");
				} else {
					moduleModuleGroupBean.setModuleGrade("系统级权限");
				}
				results.add(moduleModuleGroupBean);
			}
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {

			}

		} catch (Exception ex) {

		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return results;

	}

	/**
	 * 权限删除
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteAll(Long[] ids) {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		Statement st1 = null;
		try {
			conn = ((DataSource) SpringHelper.getBean("dataSource"))
					.getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = null;
			Vector v = new Vector();
			for (int x = 0; x < ids.length; x++) {
				// 删除角色权限对应关系
				sql = "delete from ref_module_role where module_id='" + ids[x]
						+ "'";
				st.executeUpdate(sql);
				// SysLogger.sysLogger.info(sql);
				SysLogger.sysLogger.info("I061: 权限管理-正在删除角色权限关系-角色ID='"
						+ ids[x] + "'");

				// 删除 权限数据
				sql = "delete from t_module t where t.id='" + ids[x] + "'";
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I061: 权限管理-正在删除权限-权限ID='" + ids[x]
						+ "'");
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I061: 权限管理-删除权限成功 ");
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				ret = false;
				SysLogger.sysLogger.error("E061: 权限管理-删除权限失败,SQL=" + sql, e);

			}
			SysLogger.sysLogger.error("E061: 权限管理-删除权限失败,SQL=" + sql, se);
			ret = false;
		} catch (Exception ex) {
			ret = false;
			SysLogger.sysLogger.error("E061: 权限管理-删除权限失败,SQL=" + sql, ex);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E061: 权限管理-删除权限失败-关闭Statement 失败", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E061: 权限管理-删除权限失败-关闭Connection 失败", e);
				}
			}
		}
		return ret;
	}

	public int findNextSortedIndexByParentID(Long parentID) {
		String queryString = "select count(sortedIndex) ,max(sortedIndex)  from  TModule t where parentid="
				+ parentID;
		Iterator iterator = getHibernateTemplate().find(queryString).iterator();

		if (iterator.hasNext()) {
			Object[] o = (Object[]) iterator.next();
			int count = ((Long) o[0]).intValue();
			if (count == 0)
				return 0;
			int maxsortindex = ((Long) o[1]).intValue();
			if (count > 0)
				return maxsortindex + 1;
		}
		return 0;
	}

	public TModule findLastIndexModule(Long parentID, Long sortIndex) {
		String queryString = "  from    TModule where parentid =" + parentID
				+ " and sortedIndex <" + sortIndex
				+ " order by sortedIndex desc";
		Iterator iterator = getHibernateTemplate().find(queryString).iterator();
		if (iterator.hasNext()) {
			TModule moduleTemp = (TModule) iterator.next();
			return moduleTemp;
		}
		return null;
	}

	public TModule findNextIndexModule(Long parentID, Long sortIndex) {
		String queryString = "  from  TModule where parentid=" + parentID
				+ " and sortedIndex >" + sortIndex + " order by sortedIndex";
		Iterator iterator = getHibernateTemplate().find(queryString).iterator();
		if (iterator.hasNext()) {
			Object obj = iterator.next();
			TModule moduleTemp = (TModule) obj;
			return moduleTemp;
		}
		return null;
	}

	public Page<TModule> listModule(String entCode, int page, int limitint,
			String searchValue) {
		String hql = "select t from TModule t where " +
		// "t.empCode='" + entCode+ "' and " +
				"(t.moduleName like ? or t.moduleCode like ?)";
		PageQuery<TModule> pq = new PageQuery<TModule>(this);
		Page<TModule> re = new Page<TModule>();
		re.setAutoCount(true);
		re.setPageNo(page);
		re.setPageSize(limitint);
		return pq.findByPage(re, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}

	public TRole findRoleAndModules(final Long roleId) {
		return (TRole) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session arg0)
					throws HibernateException, SQLException {
				String hql = "select r from TRole r left join fetch r.refModuleRoles ref left join fetch ref.TModule where r.id=:id";
				Query query = arg0.createQuery(hql);
				query.setParameter("id", roleId);
				return (TRole) query.uniqueResult();
			}
		});
	}

	public Page<TModule> queryRoleModulesById(Long roleId) {
		String hql = "select m from TRole r "
				+ "left join r.refModuleRoles ref "
				+ "left join ref.TModule m " + "where r.id=? order by m.sortedIndex ";
		PageQuery<TModule> pq = new PageQuery<TModule>(this);
		Page<TModule> re = new Page<TModule>();
		re.setAutoCount(true);
		re.setPageNo(1);
		re.setPageSize(Integer.MAX_VALUE);
		return pq.findByPage(re, hql, roleId);
	}
	
	public Page<TModule> queryRoleModulesByIdAndModuleGrade(Long roleId, long moduleGrade) {
		String hql = "select m from TRole r "
			+ "left join r.refModuleRoles ref "
			+ "left join ref.TModule m " + "where r.id=? and m.moduleGrade=?";
		PageQuery<TModule> pq = new PageQuery<TModule>(this);
		Page<TModule> re = new Page<TModule>();
		re.setAutoCount(true);
		re.setPageNo(1);
		re.setPageSize(Integer.MAX_VALUE);
		return pq.findByPage(re, hql, roleId, moduleGrade);
	}
}

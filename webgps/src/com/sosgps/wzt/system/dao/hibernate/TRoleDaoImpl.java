package com.sosgps.wzt.system.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TRoleDAO;
import com.sosgps.wzt.orm.TRole;
import com.sosgps.wzt.system.dao.TRoleDao;
import com.sosgps.wzt.util.PageQuery;

public class TRoleDaoImpl extends TRoleDAO implements TRoleDao{ 
	public TRoleDaoImpl(){
		super();
	}
	/**
	 * 查询用户角色表中对应删除角色的所有用户
	 * 首先删除对应用户（t_user）
	 * 其次删除用户角色对应表中数据(ref_user_role)
	 * 删除角色权限对应关系
	 * 最后删除角色表中数据(t_role)
	 * @param longIds
	 * @return
	 */
	public boolean deleteAll(Long[] ids) {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		Statement st1 = null;
		try{
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = null;
			Vector v = new Vector();
			for(int x=0 ; x<ids.length; x++){						
				//查询用户角色表中对应删除角色的所有用户
				sql = "select t.role_id as rid,t.user_id as userid from  ref_user_role t where t.role_id='"+ids[x]+"'";
				rs = st.executeQuery(sql);
				//删除角色对应用户数据
				while(rs.next()){
					v.add(rs.getLong("userid"));
				}
				//删除用户角色关系表数据
				sql = "delete from ref_user_role  where role_id='"+ids[x]+"'";
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I062: 角色管理-正在删除用户角色关系-角色ID='"+ids[x]+"'");
				
				//删除角色权限对应关系
				sql = "delete from ref_module_role where role_id='"+ids[x]+"'";
				st.executeUpdate(sql);
				//SysLogger.sysLogger.info(sql);
				SysLogger.sysLogger.info("I062: 角色管理-正在删除角色权限关系-角色ID='"+ids[x]+"'");
				
				for(int y=0; y<v.size(); y++){
					//删除用户数据
					sql = "delete from t_user  where id='"+v.get(y)+"'";
					st.executeUpdate(sql);
					SysLogger.sysLogger.info("I062: 角色管理-正在删除角色对应用户-用户ID='"+v.get(y)+"'");
				}
				
				//删除角色数据
				sql = "delete from t_role t where t.id='"+ids[x]+"'";
				st.executeUpdate(sql);
				
				SysLogger.sysLogger.info("I062: 角色管理-正在删除角色-角色ID='"+ids[x]+"'");
				
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I062: 角色管理-删除角色成功 ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E062: 角色管理-删除角色失败,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E062: 角色管理-删除角色失败,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E062: 角色管理-删除角色失败,SQL="+sql,ex);
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: 角色管理-删除角色失败-关闭Statement 失败",e);
				}		
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: 角色管理-删除角色失败-关闭Connection 失败",e);
				}				
			}
		}
		
		return ret;
	}
	
	public TRole findRoleAndModulesByRoleCode(final String entCode, final String roleCode) {
		return (TRole)getHibernateTemplate().execute(new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				String hql = "select role from TRole role " +
						"left join fetch role.refModuleRoles ref left join fetch ref.TModule module " +
						"where role.empCode=:entCode and role.roleCode=:roleCode";
				Query query = session.createQuery(hql);
				query.setParameter("entCode", entCode);
				query.setParameter("roleCode", roleCode);
				return (TRole)query.uniqueResult();
			}
		});
	}
	
	public Page<TRole> listRole(String entCode, int page, int limitint,
			String searchValue) {
		String hql = "select t from TRole t where t.empCode='" + entCode
			+ "' and (t.roleName like ? or t.roleCode like ?)";
		PageQuery<TRole> pq = new PageQuery<TRole>(this);
		Page<TRole> re = new Page<TRole>();
		re.setAutoCount(true);
		re.setPageNo(page);
		re.setPageSize(limitint);
		return pq.findByPage(re, hql, "%" + searchValue + "%", "%"
				+ searchValue + "%");
	}

	public TRole queryRoleAndModulesById(final Long roleId) {
		return (TRole) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "select r from TRole r "
						+ "left join fetch r.refModuleRoles ref "
//						+ "left join fetch ref.TModule m "
						+ "where r.id=:roleId";
				Query query = session.createQuery(hql);
				query.setParameter("roleId", roleId);
				return query.uniqueResult();
			}
		});
	}
}

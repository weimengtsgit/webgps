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
	 * ��ѯ�û���ɫ���ж�Ӧɾ����ɫ�������û�
	 * ����ɾ����Ӧ�û���t_user��
	 * ���ɾ���û���ɫ��Ӧ��������(ref_user_role)
	 * ɾ����ɫȨ�޶�Ӧ��ϵ
	 * ���ɾ����ɫ��������(t_role)
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
				//��ѯ�û���ɫ���ж�Ӧɾ����ɫ�������û�
				sql = "select t.role_id as rid,t.user_id as userid from  ref_user_role t where t.role_id='"+ids[x]+"'";
				rs = st.executeQuery(sql);
				//ɾ����ɫ��Ӧ�û�����
				while(rs.next()){
					v.add(rs.getLong("userid"));
				}
				//ɾ���û���ɫ��ϵ������
				sql = "delete from ref_user_role  where role_id='"+ids[x]+"'";
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I062: ��ɫ����-����ɾ���û���ɫ��ϵ-��ɫID='"+ids[x]+"'");
				
				//ɾ����ɫȨ�޶�Ӧ��ϵ
				sql = "delete from ref_module_role where role_id='"+ids[x]+"'";
				st.executeUpdate(sql);
				//SysLogger.sysLogger.info(sql);
				SysLogger.sysLogger.info("I062: ��ɫ����-����ɾ����ɫȨ�޹�ϵ-��ɫID='"+ids[x]+"'");
				
				for(int y=0; y<v.size(); y++){
					//ɾ���û�����
					sql = "delete from t_user  where id='"+v.get(y)+"'";
					st.executeUpdate(sql);
					SysLogger.sysLogger.info("I062: ��ɫ����-����ɾ����ɫ��Ӧ�û�-�û�ID='"+v.get(y)+"'");
				}
				
				//ɾ����ɫ����
				sql = "delete from t_role t where t.id='"+ids[x]+"'";
				st.executeUpdate(sql);
				
				SysLogger.sysLogger.info("I062: ��ɫ����-����ɾ����ɫ-��ɫID='"+ids[x]+"'");
				
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I062: ��ɫ����-ɾ����ɫ�ɹ� ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E062: ��ɫ����-ɾ����ɫʧ��,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E062: ��ɫ����-ɾ����ɫʧ��,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E062: ��ɫ����-ɾ����ɫʧ��,SQL="+sql,ex);
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: ��ɫ����-ɾ����ɫʧ��-�ر�Statement ʧ��",e);
				}		
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: ��ɫ����-ɾ����ɫʧ��-�ر�Connection ʧ��",e);
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

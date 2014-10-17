package com.sosgps.wzt.system.dao.hibernate;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.TModuleGroup;
import com.sosgps.wzt.orm.TModuleGroupDAO;
import com.sosgps.wzt.system.dao.TModuleGroupDao;
/**
 * @Title:Ȩ����Dao����
 * @Description:
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-20 ����02:41:11
 */
public class TModuleGroupDaoImpl extends TModuleGroupDAO implements TModuleGroupDao {

	
	public TModuleGroupDaoImpl(){
		super();
	}
	/**
	 * ����ɾ��Ȩ����Ȩ�޹�ϵ
	 * ���ɾ������Ȩ��
	 * ���ɾ��Ȩ����
	 */
	public boolean deleteAll(Long[] ids) throws Exception {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		Statement st1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try{
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			st1=conn.createStatement();;
			Vector v = new Vector();
			for(int x=0 ; x<ids.length; x++){						
				
				sql = "select a.id,a.module_id,a.module_name from t_module a where a.module_id='"+ids[x]+"'";
				
				rs = st.executeQuery(sql);
				//ɾ����ӦȨ��Ȩ�����ϵ
				while(rs.next()){
					Long module_id =rs.getLong("id");
					sql = "delete from ref_module_role t where t.module_id='"+module_id+"'";
					st1.execute(sql);		
				}
				sql = "delete from t_module t where t.module_id='"+ids[x]+"'";

				rs = st.executeQuery(sql);
				sql = "delete from t_module_group t where t.id='"+ids[x]+"'";
				
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I064: Ȩ�������-����ɾ��Ȩ�����ϵ-Ȩ����ID='"+ids[x]+"'");
								
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I064: Ȩ�������-ɾ��Ȩ����ɹ� ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��,SQL="+sql,ex);
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��-�ر�Statement ʧ��",e);
				}		
			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��-�ر�Statement ʧ��",e);
				}
			}
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��-�ر�Statement ʧ��",e);
				}		
			}
			if(st1 != null){
				try{
					st1.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��-�ر�Statement ʧ��",e);
				}
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: Ȩ�������-ɾ��Ȩ����ʧ��-�ر�Connection ʧ��",e);
				}				
			}
		}
		
		return ret;
	}

}
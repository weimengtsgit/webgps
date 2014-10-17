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
 * @Title:权限组Dao管理
 * @Description:
 * @Copyright: Copyright (c) 2007
 * @Company: Mapabc
 * @author: zhangwei
 * @version 1.0
 * @date: 2008-8-20 下午02:41:11
 */
public class TModuleGroupDaoImpl extends TModuleGroupDAO implements TModuleGroupDao {

	
	public TModuleGroupDaoImpl(){
		super();
	}
	/**
	 * 首先删除权限组权限关系
	 * 其次删除所属权限
	 * 最后删除权限组
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
				//删除对应权限权限组关系
				while(rs.next()){
					Long module_id =rs.getLong("id");
					sql = "delete from ref_module_role t where t.module_id='"+module_id+"'";
					st1.execute(sql);		
				}
				sql = "delete from t_module t where t.module_id='"+ids[x]+"'";

				rs = st.executeQuery(sql);
				sql = "delete from t_module_group t where t.id='"+ids[x]+"'";
				
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I064: 权限组管理-正在删除权限组关系-权限组ID='"+ids[x]+"'");
								
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I064: 权限组管理-删除权限组成功 ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败,SQL="+sql,ex);
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败-关闭Statement 失败",e);
				}		
			}
			if(rs1 != null){
				try{
					rs1.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败-关闭Statement 失败",e);
				}
			}
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败-关闭Statement 失败",e);
				}		
			}
			if(st1 != null){
				try{
					st1.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败-关闭Statement 失败",e);
				}
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E064: 权限组管理-删除权限组失败-关闭Connection 失败",e);
				}				
			}
		}
		
		return ret;
	}

}
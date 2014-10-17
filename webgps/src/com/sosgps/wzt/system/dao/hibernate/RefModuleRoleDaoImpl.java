package com.sosgps.wzt.system.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.orm.RefModuleRole;
import com.sosgps.wzt.orm.RefModuleRoleDAO;
import com.sosgps.wzt.system.dao.RefModuleRoleDao;
/**
 * 权限角色关系
 * @author Administrator
 *
 */
public class RefModuleRoleDaoImpl extends RefModuleRoleDAO implements RefModuleRoleDao{
	public boolean delete(Long id) {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		try{
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = null;
								
				
				//删除用户角色关系表数据
				sql = "delete from ref_module_role  where role_id='"+id+"'";
				st.executeUpdate(sql);
				SysLogger.sysLogger.info("I062: 角色管理-正在删除用户角色关系-角色ID='"+id+"'");
	
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I062: 角色管理-删除用户角色关系 ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E062: 角色管理-删除删除用户角色关系失败,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E062: 角色管理-删除删除用户角色关系失败,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E062: 角色管理-删除删除用户角色关系失败,SQL="+sql,ex);
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: 角色管理-删除删除用户角色关系失败-关闭Statement 失败",e);
				}		
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E062: 角色管理-删除删除用户角色关系失败-关闭Connection 失败",e);
				}				
			}
		}
		
		return ret;
	}
}

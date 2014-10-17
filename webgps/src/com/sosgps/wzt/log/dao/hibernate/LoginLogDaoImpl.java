package com.sosgps.wzt.log.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.hibernate.Session;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.dao.LoginLogDao;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TLoginLog;
import com.sosgps.wzt.orm.TLoginLogDAO;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.PageQuery;
/**
 * <p>Title: LoginLogDaoImpl</p>
 * <p>Description: 登录日志</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: 图盟科技</p>
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class LoginLogDaoImpl extends TLoginLogDAO  implements LoginLogDao{
	public  LoginLogDaoImpl (){
		super();
	}
	public Page<TLoginLog> queryLoginLog(String entCode,
			String deviceIds, String startTime, String endTime, String pageNo,
			String pageSize, String paramName, String paramValue, String vague,
			boolean autoCount, String type){
		Page<TLoginLog> re = new Page<TLoginLog>();
		
		int page = 1;
		int pageS = 10;
		if (pageNo == null) {
			pageNo = "1";
		}
		if (pageSize == null) {
			pageSize = "10";
		}
		try {
			page = Integer.parseInt(pageNo);
			pageS = Integer.parseInt(pageSize);
		} catch (Exception e) {
			SysLogger.sysLogger.error(Constants.ERROR_TYPE_TRANSFORM_WRONG, e);
			return re;
		}
		try {
			re.setAutoCount(autoCount);
			re.setPageNo(page);
			re.setPageSize(pageS);
			String hql = "from TLoginLog t where t.loginTime between ? and ? and t.empCode = ?";
			PageQuery<TLoginLog> pageQuery=new PageQuery<TLoginLog>(this);
			Date begin = DateUtility.strToDateTime(startTime+" 00:00:00");
			Date end = DateUtility.strToDateTime(endTime+" 23:59:59"); 
			if (paramName != null && !paramName.equals("")) {
				if (vague == null || vague.equalsIgnoreCase("y")) {
					hql += " and " + paramName + " like ? order by t.loginTime";
					re = pageQuery.findByPage(re, hql, deviceIds,
							begin, end,entCode, "%" + paramValue + "%");
				} else {
					hql += " and " + paramName + "=? order by t.loginTime";
					re = pageQuery.findByPage(re, hql, deviceIds,
							begin, end,entCode, paramValue);
				}
			} else {
				hql += " order by t.loginTime desc";
				re = pageQuery.findByPage(re, hql, begin, end,entCode);
			}
		} catch (Exception e) {
			SysLogger.sysLogger.error("登陆日志 " + Constants.ERROR_DAO, e);
		}		
		
		return re;		
	}
	public boolean deleteAll(Long[] ids){
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
		
			for(int x=0 ; x<ids.length; x++){										
				//删除角色日志对应关系
				sql = "delete from t_login_log where id='"+ids[x]+"'";
				st.executeUpdate(sql);
				//SysLogger.sysLogger.info(sql);
				SysLogger.sysLogger.info("I065: 登录日志管理-正在删除日志-日志ID='"+ids[x]+"'");
								
				
			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I065: 登录日志管理-删除日志成功 ");
		}catch(SQLException se){
			try{
				conn.rollback();
			}catch(SQLException e){
				ret = false;
				SysLogger.sysLogger.error("E065: 登录日志管理-删除日志失败,SQL="+sql,e);
				
			}
			SysLogger.sysLogger.error("E065: 登录日志管理-删除日志失败,SQL="+sql,se);
			ret = false;
		}catch(Exception ex){
			ret = false;
			SysLogger.sysLogger.error("E065: 登录日志管理-删除日志失败,SQL="+sql,ex);
		}finally{
			if(st != null){
				try{
					st.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E065: 登录日志管理-删除日志失败-关闭Statement 失败",e);
				}		
			}
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					ret = false;
					SysLogger.sysLogger.error("E065: 登录日志管理-删除日志失败-关闭Connection 失败",e);
				}				
			}
		}	
		return ret;
	}

	public Page<TLoginLog> listLoginLog(String entCode, int pageNo,
			int pageSize, Date startTime, Date endTime, String searchValue) {
		String hql = "from TLoginLog t where t.empCode = ? "
				+ "and t.loginTime between ? and ? " + "and t.userName like ? "
				+ "order by t.loginTime desc";
		PageQuery<TLoginLog> pq = new PageQuery<TLoginLog>(this);
		Page<TLoginLog> re = new Page<TLoginLog>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, entCode, startTime, endTime, "%"
				+ searchValue + "%");
	}
}

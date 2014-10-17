package com.sosgps.wzt.log.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import javax.sql.DataSource;

import org.sos.helper.SpringHelper;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sosgps.wzt.log.SysLogger;
import com.sosgps.wzt.log.dao.OptLogDao;
import com.sosgps.wzt.manage.common.Constants;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TOptLog;
import com.sosgps.wzt.orm.TOptLogDAO;
import com.sosgps.wzt.util.DateUtility;
import com.sosgps.wzt.util.PageQuery;

/**
 * <p>
 * Title: OptLogDao
 * </p>
 * <p>
 * Description: 操作日志Dao实现类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: 图盟科技
 * </p>
 * 
 * @author 位置通项目组 张卫
 * @version 1.0
 */
public class OptLogDaoImpl extends TOptLogDAO implements OptLogDao {
	public OptLogDaoImpl() {
		super();
	}

	public Page<TOptLog> queryOptLog(String entCode, String deviceIds,
			String startTime, String endTime, String pageNo, String pageSize,
			String paramName, String paramValue, String vague,
			boolean autoCount, String type) {
		Page<TOptLog> re = new Page<TOptLog>();

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
			String hql = "from TOptLog t where t.optTime between ? and ? and t.empCode = ? ";
			PageQuery<TOptLog> pageQuery = new PageQuery<TOptLog>(this);
			Date begin = DateUtility.strToDateTime(startTime + " 00:00:00");
			Date end = DateUtility.strToDateTime(endTime + " 23:59:59");
			if (paramName != null && !paramName.equals("")) {
				if (vague == null || vague.equalsIgnoreCase("y")) {
					hql += " and " + paramName + " like ? order by t.optTime";
					re = pageQuery.findByPage(re, hql, deviceIds, begin, end,
							entCode, "%" + paramValue + "%");
				} else {
					hql += " and " + paramName + "=? order by t.optTime";
					re = pageQuery.findByPage(re, hql, deviceIds, begin, end,
							entCode, paramValue);
				}
			} else {
				hql += " order by t.optTime desc";
				re = pageQuery.findByPage(re, hql, begin, end, entCode);
			}
		} catch (Exception e) {
			SysLogger.sysLogger.error("操作日志 " + Constants.ERROR_DAO, e);
		}

		return re;
	}

	public boolean deleteAll(Long[] ids) {
		boolean ret = true;
		Connection conn = null;
		String sql = "";
		Statement st = null;
		Statement st1 = null;
		try {
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			ResultSet rs = null;

			for (int x = 0; x < ids.length; x++) {
				// 删除角色日志对应关系
				sql = "delete from t_opt_log where id='" + ids[x] + "'";
				st.executeUpdate(sql);
				// SysLogger.sysLogger.info(sql);
				SysLogger.sysLogger.info("I065: 操作日志管理-正在删除日志-日志ID='" + ids[x]
						+ "'");

			}
			conn.commit();
			conn.setAutoCommit(true);
			SysLogger.sysLogger.info("I065: 操作日志管理-删除日志成功 ");
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				ret = false;
				SysLogger.sysLogger.error("E065: 操作日志管理-删除日志失败,SQL=" + sql, e);

			}
			SysLogger.sysLogger.error("E065: 操作日志管理-删除日志失败,SQL=" + sql, se);
			ret = false;
		} catch (Exception ex) {
			ret = false;
			SysLogger.sysLogger.error("E065: 操作日志管理-删除日志失败,SQL=" + sql, ex);
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E065: 操作日志管理-删除日志失败-关闭Statement 失败", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					ret = false;
					SysLogger.sysLogger.error(
							"E065: 操作日志管理-删除日志失败-关闭Connection 失败", e);
				}
			}
		}
		return ret;
	}

	public Page<TOptLog> listOptLog(String entCode, int pageNo, int pageSize,
			Date startTime, Date endTime, String searchValue) {
		String hql = "from TOptLog t where t.empCode = ? "
				+ "and t.optTime between ? and ? " + "and t.userName like ? "
				+ "order by t.optTime desc";
		PageQuery<TOptLog> pq = new PageQuery<TOptLog>(this);
		Page<TOptLog> re = new Page<TOptLog>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		return pq.findByPage(re, hql, entCode, startTime, endTime, "%"
				+ searchValue + "%");
	}
}

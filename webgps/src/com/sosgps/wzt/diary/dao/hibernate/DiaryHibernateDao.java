package com.sosgps.wzt.diary.dao.hibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.sos.helper.SpringHelper;

import com.sosgps.wzt.diary.dao.DiaryDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryDAO;
import com.sosgps.wzt.util.PageQuery;

public class DiaryHibernateDao extends TDiaryDAO implements DiaryDao {
	private static final Logger logger = Logger.getLogger(DiaryHibernateDao.class);

	public Page<Object[]> listDiaryByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId) {
		String hql = " select td.id, td.deviceId, td.title, td.content, td.remark," 
			+ " td.createDate, td.modifyDate, td.entCode, td.userId, td.diaryDate, td.remarkDate "
			+ " from TDiary td "
			+ " where td.entCode = ? "
			//+ " and td.userId = ? "
			+ " and td.deviceId = ? "
			+ " and td.diaryDate between ? and ? "
			+ " and ( title like '%"+searchValue+"%'  or content like '%"+searchValue+"%' ) "
			+ " order by td.diaryDate desc, td.userId ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, deviceId, startDate, endDate);
	}
	
	public Page<Object[]> listDiaryByDeviceIds(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId) {
		String hql = " select td.id, td.termName, td.title, td.content, td.remark," 
			+ " td.createDate, td.modifyDate, td.entCode, td.userId, td.diaryDate, td.remarkDate, td.deviceId "
			+ " from TDiary td "
			+ " where td.entCode = ? "
			+ " and td.diaryDate between ? and ? "
			+ " and ( title like '%"+searchValue+"%'  or content like '%"+searchValue+"%' ) ";
			if(deviceId != null && deviceId.length() > 0){
				hql+= " and td.deviceId in ("+deviceId+") ";
			}
			hql+= " order by  td.deviceId, td.diaryDate desc ";
			
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, startDate, endDate);
	}
	
	public long getCurrentSequence() {
		long seq = 0;
		Connection conn = null;
		String sql = "SELECT SEQ_DIARY.NEXTVAL as seq FROM DUAL";
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = ( (DataSource)SpringHelper.getBean("dataSource")).getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				seq = rs.getLong("seq");
			}
			conn.commit();
			conn.setAutoCommit(true);
			logger.info("日志---查询日志序列号成功 ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("日志---获取当前序列号，关闭ResultSet 失败");
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("日志---关闭Statement 失败", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("日志---关闭Connection 失败", e);
				}
			}
		}
		return seq;
	}
}

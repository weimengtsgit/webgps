package com.sosgps.wzt.diarymark.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.wzt.diarymark.dao.DiaryMarkDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDiaryMark;
import com.sosgps.wzt.orm.TDiaryMarkDAO;
import com.sosgps.wzt.util.PageQuery;

public class DiaryMarkHibernateDao extends TDiaryMarkDAO implements DiaryMarkDao {
	private static final Logger logger = Logger.getLogger(DiaryMarkHibernateDao.class);
	
	public void deleteByDeviceId(final String diaryId) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					String hql = " delete TDiaryMark t " 
					+ " where t.diaryId = '"+diaryId+"' " ;
					Query query = s.createQuery(hql);
					//query.setParameter("ids", ids);
					query.executeUpdate();
					return null;
				}
			});
		} catch (RuntimeException re) {
			logger.error(re);
			throw re;
		}
	}
	
	public Page<TDiaryMark> listDiaryMarkById(String entCode, Long userId, int pageNo, int pageSize, Long id) {
		String hql = " select tdm "
			+ " from TDiaryMark tdm "
			+ " where tdm.diaryId = ? "
			+ " order by tdm.diaryDate desc ";
		PageQuery<TDiaryMark> pageQuery = new PageQuery<TDiaryMark>(this);
		Page<TDiaryMark> page = new Page<TDiaryMark>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, id);
	}
	
	public Page<TDiaryMark> listDiaryMarkByDeviceId(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String deviceId) {
		String hql = " select tdm "
			+ " from TDiaryMark tdm "
			+ " where tdm.entCode = ? "
			//+ " and td.userId = ? "
			+ " and tdm.diaryDate between ? and ? "
			+ " and tdm.deviceId = ? "
			//+ " and ( tdm.title like '%"+searchValue+"%'  or tdm.content like '%"+searchValue+"%' ) "
			+ " order by tdm.diaryDate desc ";
		PageQuery<TDiaryMark> pageQuery = new PageQuery<TDiaryMark>(this);
		Page<TDiaryMark> page = new Page<TDiaryMark>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, startDate, endDate, deviceId);
	}
	
	public Page<Object[]> listDiaryByUser(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = " select td.id, td.termName, td.title, td.content, td.remark," 
			+ " td.createDate, td.modifyDate, td.entCode, td.userId, td.diaryDate, td.remarkDate "
			+ " from TDiary td "
			+ " where td.entCode = ? "
			+ " and td.userId = ? "
			+ " and td.diaryDate between ? and ? "
			+ " and ( title like '%"+searchValue+"%'  or content like '%"+searchValue+"%' ) "
			+ " order by td.diaryDate desc, td.userId ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, userId, startDate, endDate);
	}
	
	public Page<Object[]> listDiaryByUsers(String entCode, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue, String termName) {
		String hql = " select td.id, td.termName, td.title, td.content, td.remark," 
			+ " td.createDate, td.modifyDate, td.entCode, td.userId, td.diaryDate, td.remarkDate "
			+ " from TDiary td "
			+ " where td.entCode = ? "
			+ " and td.diaryDate between ? and ? "
			+ " and ( title like '%"+searchValue+"%'  or content like '%"+searchValue+"%' ) "
			+ " and td.termName in ("+termName+") "
			+ " order by td.diaryDate desc, td.userId ";
		PageQuery<Object[]> pageQuery = new PageQuery<Object[]>(this);
		Page<Object[]> page = new Page<Object[]>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, entCode, startDate, endDate);
	}

}

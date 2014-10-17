package com.sosgps.v21.dao.impl.hibernate;

import org.hibernate.Query;

import com.sosgps.v21.dao.EmailSendLogDAO;
import com.sosgps.v21.model.EmailSendLog;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.util.PageQuery;

public class EmailSendLogDaoImpl extends BaseHibernateDao implements
		EmailSendLogDAO {

	//
	public Page<EmailSendLog> listEmailLogs(Long startDate, Long endDate,
			String entCode, String entName, String contactName, int pageNo,
			int pageSize) {

		String hql = "select t from EmailSendLog t "
				+ " where t.createon >= ? and t.createon < ? "
				+ " and t.entCode like ? " + " and t.entName like ? "
				+ " and t.contactName like ? " + " and t.status = 0 "
				+ " order by t.createon desc, t.entCode, t.contactName";
		PageQuery<EmailSendLog> pageQuery = new PageQuery<EmailSendLog>(this);
		Page<EmailSendLog> page = new Page<EmailSendLog>();
		page.setAutoCount(true);
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ entCode + "%", "%" + entName + "%", "%" + contactName + "%");
	}

	public Page<EmailSendLog> listEmailLogs(Long startDate, Long endDate,
			String entCode, String entName, String contactName) {

		String hql = "select t from EmailSendLog t "
				+ " where t.createon >= ? and t.createon < ? "
				+ " and t.entCode like ? " + " and t.entName like ? "
				+ " and t.contactName like ? " + " and t.status = 0 "
				+ " order by t.createon desc, t.entCode, t.contactName";
		PageQuery<EmailSendLog> pageQuery = new PageQuery<EmailSendLog>(this);
		Page<EmailSendLog> page = new Page<EmailSendLog>();
		page.setAutoCount(false);
		return pageQuery.findByPage(page, hql, startDate, endDate, "%"
				+ entCode + "%", "%" + entName + "%", "%" + contactName + "%");
	}

	public void deleteEmailLogs(Long[] ids) {
		try {
			StringBuilder hql = new StringBuilder(
					"update EmailSendLog t set t.status = 1 where t.id in (?1)");
			Query query = super.getSession().createQuery(hql.toString());
			query.setParameterList("1", ids);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("[deleting email logs] error", e);
		}

	}
}

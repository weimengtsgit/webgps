package com.sosgps.wzt.annualExamination.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.wzt.annualExamination.dao.AnnualExaminationDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TAnnualExaminationDao;
import com.sosgps.wzt.util.PageQuery;

public class AnnualExaminationDaoImpl extends TAnnualExaminationDao implements AnnualExaminationDao {

	public Page<Object[]> listAnnualExamination(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = "select to.id, t.termName, t.vehicleNumber, t.simcard, g.groupName, to " +
				" from TTerminal t, TAnnualExamination to "
				+ " left join t.refTermGroups ref "
				+ " left join ref.TTermGroup g "
				+ " where to.empCode = ? " 
				//+ "and to.userId = ?  "
				+ " and (t.termName like ? or t.vehicleNumber like ? or g.groupName like ?) "
				+ " and to.examinationDate >= ? and to.examinationDate <= ? "
				+ " and to.deviceId = t.deviceId "
				+ " order by to.examinationDate desc, t.termName ";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		//return pq.findByPage( re, hql, entCode, userId, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		return pq.findByPage( re, hql, entCode, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		
	}
	
	public void deleteAll(final String ids) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("delete TAnnualExamination t where t.id in ("+ids+")");
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

}

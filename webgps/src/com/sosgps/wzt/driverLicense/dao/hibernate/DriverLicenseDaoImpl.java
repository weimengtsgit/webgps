package com.sosgps.wzt.driverLicense.dao.hibernate;

import java.sql.SQLException;
import java.util.Date;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import com.sosgps.wzt.driverLicense.dao.DriverLicenseDao;
import com.sosgps.wzt.manage.util.Page;
import com.sosgps.wzt.orm.TDriverLicenseDao;
import com.sosgps.wzt.util.PageQuery;

public class DriverLicenseDaoImpl extends TDriverLicenseDao implements DriverLicenseDao {

	public Page<Object[]> listDriverLicense(String entCode, Long userId, int pageNo, int pageSize, Date startDate, Date endDate, String searchValue) {
		String hql = "select to " +
				" from TDriverLicense to "
				+ " where to.empCode = ? " 
				//+ "and to.userId = ?  "
				+ " and to.examinationDate >= ? and to.examinationDate <= ? "
				+ " and ( to.employeeNum like ? or to.employeeWorknum like ? " 
				+ " or to.employeeName like ? or to.employeeId like ? or to.department like ? " 
				+ " or to.condition like ? ) "
				+ " order by to.examinationDate desc ";
		PageQuery<Object[]> pq = new PageQuery<Object[]>(this);
		Page<Object[]> re = new Page<Object[]>();
		re.setAutoCount(true);
		re.setPageNo(pageNo);
		re.setPageSize(pageSize);
		//return pq.findByPage( re, hql, entCode, userId, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", startDate, endDate );
		return pq.findByPage( re, hql, entCode, startDate, endDate, "%" + searchValue + "%", "%" + searchValue + "%"
				, "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%", "%" + searchValue + "%");
		
	}
	
	public void deleteAll(final String ids) {
		try {
			getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session s)
						throws HibernateException, SQLException {
					Query query = s.createQuery("delete TDriverLicense t where t.id in ("+ids+")");
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
